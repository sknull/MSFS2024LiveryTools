package de.visualdigits.msfs2024tools.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.onError
import de.visualdigits.common.domain.model.onSuccess
import de.visualdigits.common.domain.util.toUiText
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.SK
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage.Companion.log
import de.visualdigits.msfs2024tools.domain.model.type.Conversion
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.domain.model.type.SimType
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository
import de.visualdigits.msfs2024tools.domain.service.Msfs2024Service
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.error_global_configuration_invalid
import msfs2024liverytools.composeapp.generated.resources.error_project_configuration_invalid
import msfs2024liverytools.composeapp.generated.resources.warning_migration
import java.io.File
import java.util.Locale

class Msfs2024ToolsViewModel(
    val configurationRepository: ConfigurationRepository,
    val msfs2024Service: Msfs2024Service,
) : ViewModel() {

    companion object {

        private const val MAX_LOG_LINES = 200
    }

    init {
        Logger.i("Application has started.")
    }

    private val _state = MutableStateFlow(Msfs2024ToolsState())
    val state: StateFlow<Msfs2024ToolsState> = _state
        .onStart {
            if (_state.value.settings == null ) {
                loadConfiguration()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onAction(action: Msfs2024ToolsAction) {
        when (action) {

            //
            // Settings
            //
            is Msfs2024ToolsAction.OnEditSettingsClick -> {
                _state.update {
                    it.copy(
                        originalSettings = it.settings,
                        isEditingSettings = action.isEditingSettings,
                        isShowInfos = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnSettingsValueChanged -> {
                if (action.keyValue.descriptor.key == SK.language) {
                    action.keyValue.value?.also { l ->
                        Locale.setDefault(Language.valueOf(l).locale)
                    }
                }
                _state.update {
                    val settings = action.settings?.copy(
                        key = action.keyValue.descriptor.key as SK,
                        value = action.keyValue.value
                    )
                    it.copy(
                        settings = settings,
                    )
                }
            }

            is Msfs2024ToolsAction.OnEditSettingsCancelClick -> {
                _state.update { state ->
                    state.originalSettings?.get<Language>(SK.language)?.also { l -> Locale.setDefault(l.locale) }
                    state.copy(
                        settings = state.originalSettings?.copy(),
                        isEditingSettings = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnSaveSettingsClick -> {
                saveSettings(
                    settings = action.settings,
                    projectConfigurations = action.projectConfigurations,
                )
            }

            is Msfs2024ToolsAction.OnSaveAirplanesClick -> {
                val newAirplanes = action.settings?.get<MutableList<String>>(SK.airplanes)
                newAirplanes?.removeIf { a -> a.isBlank() }
                saveSettings(
                    settings = action.settings?.copy(SK.airplanes, newAirplanes?.joinToString(",")),
                    projectConfigurations = action.projectConfigurations
                )

                updateProjectConfigurations(action.projectConfigurations.filterNot { p -> p.get<String>(PK.airplaneName) == null || p.get<String>(PK.liveryName) == null })
            }


            //
            // Project Configuration
            //
            is Msfs2024ToolsAction.OnProjectClick -> {
                _state.update {
                    it.copy(
                        currentProjectConfiguration = action.projectConfiguration,
                        isEditingSettings = false,
                        isEditingProjectConfiguration = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnNewProjectClick -> {
                _state.update {
                    val project = ProjectConfiguration(settings = it.settings)
                    it.copy(
                        originalProjectConfiguration = null,
                        currentProjectConfiguration = project,
                        projectConfigurations = it.projectConfigurations + project,
                        isEditingProjectConfiguration = true,
                        isNewProject = true,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnEditProjectConfigurationClick -> {
                _state.update {
                    it.copy(
                        originalProjectConfiguration = it.currentProjectConfiguration?.copy(),
                        isEditingProjectConfiguration = true,
                        isNewProject = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnProjectConfigurationValueChanged -> {
                _state.update {
                    val currentProjectConfiguration = action.projectConfiguration?.copy(
                        key = action.keyValue.descriptor.key as PK,
                        value = action.keyValue.value
                    )
                    it.copy(
                        currentProjectConfiguration = currentProjectConfiguration
                    )
                }
            }

            is Msfs2024ToolsAction.OnEditProjectConfigurationCancelClick -> {
                _state.update {
                    it.copy(
                        currentProjectConfiguration = action.originalProjectConfiguration,
                        projectConfigurations = if (it.isNewProject) it.currentProjectConfiguration?.let { pp -> it.projectConfigurations - pp }?:it.projectConfigurations else it.projectConfigurations,
                        originalProjectConfiguration = null,
                        isEditingProjectConfiguration = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnSaveProjectConfigurationClick -> {
                saveProjectConfiguration(
                    projectConfiguration = action.projectConfiguration
                )
            }

            is Msfs2024ToolsAction.OnDeleteProjectClick -> {
                deleteProject(
                    projectConfiguration = action.projectConfiguration,
                )
            }

            //
            //
            //
            is Msfs2024ToolsAction.OnShowInfosClick -> {
                _state.update {
                    it.copy(
                        originalSettings = it.settings,
                        isEditingSettings = false,
                        isShowInfos = action.isShowInfos,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnInitializeTabs -> {
                _state.update {
                    it.copy(
                        tabLabels = action.tabLabels,
                        selectedTabIndex = 0,
                        selectedTabLabel = action.tabLabels.firstOrNull(),
                        isEditingSettings = false,
                        isShowInfos = false,
                        isEditingProjectConfiguration = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnTabSelected -> {
                _state.update {
                    it.copy(
                        selectedTabIndex = action.index,
                        selectedTabLabel = it.tabLabels[action.index],
                        isEditingSettings = false,
                        isShowInfos = false,
                        isEditingProjectConfiguration = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnLanguageSelected -> {
                _state.update {
                    it.copy(
                        settings = it.settings?.copy(key = SK.language, value = action.language.name)
                    )
                }
            }

            is Msfs2024ToolsAction.OnPanelOkClick -> {
                _state.update {
                    it.copy(
                        currentProjectConfiguration = null,
                        isEditingSettings = false,
                        isEditingProjectConfiguration = false
                    )
                }
            }

            is Msfs2024ToolsAction.OnBusyOkClick -> {
                _state.update {
                    it.copy(
                        isEditingSettings = false,
                        isEditingProjectConfiguration = false,
                        isLoading = false,
                        isConverting = false,
                        logs = listOf()
                    )
                }
            }

            is Msfs2024ToolsAction.OnConversionClick -> {
                executeConversion(
                    settings = action.settings?.copy(),
                    projectConfiguration = action.currentProjectConfiguration.copy(),
                    conversion = action.conversion,
                    dryRun = action.dryRun,
                    progress = { p -> _state.update {
                        it.copy(
                            currentProgress = p,
                        )
                    }
                    },
                    logger = { lm ->
                        log(lm)
                        _state.update {
                            it.copy(
                                logs = (it.logs + lm).takeLast(MAX_LOG_LINES)
                            )
                        }
                    }
                )
            }
        }
    }

    private fun loadConfiguration() = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true,
            )
        }
        configurationRepository.loadConfiguration()
            .onSuccess { (settings, projectConfigurations, migrated) ->
                Locale.setDefault(settings.get<Language>(SK.language)?.locale?: Language.EN.locale)
                _state.update {
                    it.copy(
                        settings = settings,
                        projectConfigurations = projectConfigurations,
                        isLoading = false,
                        uiMessage = if (migrated) UiText.StringResourceId(Res.string.warning_migration) else null,
                        uiMessageSeverity = if (migrated) Severity.Info else null
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        uiMessage = error.toUiText(),
                        uiMessageSeverity = Severity.Error
                    )
                }
            }
    }

    private fun executeConversion(
        settings: Settings?,
        projectConfiguration: ProjectConfiguration?,
        conversion: Conversion,
        dryRun: Boolean,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit,
    ) = viewModelScope.launch {
        if (projectConfiguration == null) {
            return@launch
        }
        _state.update {
            it.copy(
                isConverting = true,
            )
        }
        msfs2024Service.executeConversion(
            configuration = settings,
            project = projectConfiguration,
            conversion = conversion,
            dryRun = dryRun,
            progress = progress,
            logger = logger,
        )
            .onSuccess {
                _state.update {
                    it.copy(
                        currentProgress = 0.0f,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        currentProgress = 0.0f,
                        uiMessage = error.toUiText(),
                        uiMessageSeverity = null
                    )
                }
            }
    }

    private fun saveSettings(
        settings: Settings?,
        projectConfigurations: List<ProjectConfiguration>
    ) = viewModelScope.launch {
        if (settings == null || settings.get<SimType>(SK.simType) == null) {
            _state.update {
                it.copy(
                    uiMessage = UiText.StringResourceId(Res.string.error_global_configuration_invalid),
                    uiMessageSeverity = Severity.Error
                )
            }

            return@launch
        }

        // update settings in all projects
        projectConfigurations.forEach { p ->
            p.settings = settings
        }

        _state.update {
            it.copy(
                isLoading = true,
            )
        }

        configurationRepository.saveSettings(settings)
            .onSuccess {
                _state.update {
                    it.copy(
                        settings = settings,
                        isLoading = false,
                        isEditingSettings = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        uiMessage = error.toUiText(),
                        uiMessageSeverity = Severity.Error
                    )
                }
            }
    }

    private fun saveProjectConfiguration(
        projectConfiguration: ProjectConfiguration?,
    ) = viewModelScope.launch {
        if (projectConfiguration == null || projectConfiguration.get<String>(PK.airplaneName) == null || projectConfiguration.get<String>(PK.liveryName) == null) {
            _state.update {
                it.copy(
                    uiMessage = UiText.StringResourceId(Res.string.error_project_configuration_invalid),
                    uiMessageSeverity = Severity.Error
                )
            }

            return@launch
        }

        _state.update {
            it.copy(
                isLoading = true,
            )
        }

        projectConfiguration.get<File>(PK.packageTextureDir)?.also { dir ->
            configurationRepository.determineTextureFormat(dir)
                .onSuccess { textureFormat ->
                    projectConfiguration.set(PK.textureFormatPackage, textureFormat)
                }
                .onError {error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            uiMessage = error.toUiText(),
                            uiMessageSeverity = Severity.Error
                        )
                    }
                }
        }

        projectConfiguration.get<File>(PK.modelTexturesDir)?.also { dir ->
            configurationRepository.determineTextureFormat(dir)
                .onSuccess { textureFormat ->
                    projectConfiguration.set(PK.textureFormatModel, textureFormat)
                }
                .onError {error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            uiMessage = error.toUiText(),
                            uiMessageSeverity = Severity.Error
                        )
                    }
                }
        }

        configurationRepository.saveProjectConfiguration(projectConfiguration)
            .onSuccess {
                _state.update { state ->
                    val projectConfigurations = state.projectConfigurations
                        .filterNot { p ->
                            (p.get<String>(PK.airplaneName) == projectConfiguration.get<String>(PK.airplaneName) && p.get<String>(PK.liveryName) == projectConfiguration.get<String>(PK.liveryName))
                                    || (p.get<String>(PK.airplaneName) == null && p.get<String>(PK.liveryName) == null)
                        } + projectConfiguration
                    state.copy(
                        currentProjectConfiguration = projectConfiguration,
                        projectConfigurations = projectConfigurations,
                        isLoading = false,
                        isEditingProjectConfiguration = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        uiMessage = error.toUiText(),
                        uiMessageSeverity = Severity.Error
                    )
                }
            }
    }

    private fun updateProjectConfigurations(
        projectConfigurations: List<ProjectConfiguration>,
    ) = viewModelScope.launch {
        if (projectConfigurations.isEmpty()) {
            return@launch
        }

        _state.update {
            it.copy(
                isLoading = true,
            )
        }

        configurationRepository.updateProjectConfigurations(projectConfigurations)
            .onSuccess {
                _state.update { state ->
                    state.copy(
                        projectConfigurations = projectConfigurations,
                        isLoading = false,
                        isEditingProjectConfiguration = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        uiMessage = error.toUiText(),
                        uiMessageSeverity = Severity.Error
                    )
                }
            }
    }

    private fun deleteProject(
        projectConfiguration: ProjectConfiguration?
    ) = viewModelScope.launch {
        if (projectConfiguration == null) {
            return@launch
        }

        _state.update {
            it.copy(
                isLoading = true,
            )
        }

        configurationRepository.deleteProjectConfiguration(projectConfiguration)
            .onSuccess {
                _state.update {
                    it.copy(
                        currentProjectConfiguration = null,
                        projectConfigurations = (it.projectConfigurations - projectConfiguration),
                        isLoading = false,
                        uiMessage = null,
                        uiMessageSeverity = null,
                        isEditingSettings = false,
                        isEditingProjectConfiguration = false,
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        uiMessage = error.toUiText(),
                        uiMessageSeverity = Severity.Error
                    )
                }
            }
    }
}
