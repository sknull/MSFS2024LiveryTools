package de.visualdigits.msfs2024tools.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.log
import de.visualdigits.common.domain.model.errorhandling.Result
import de.visualdigits.common.domain.model.errorhandling.onError
import de.visualdigits.common.domain.model.errorhandling.onSuccess
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.error_global_configuration_invalid
import de.visualdigits.compose.resources.error_project_configuration_invalid
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.SK
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.toUiText
import de.visualdigits.msfs2024tools.domain.model.type.Conversion
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.domain.model.type.SimType
import de.visualdigits.msfs2024tools.domain.service.SettingsRepository
import de.visualdigits.msfs2024tools.domain.service.Msfs2024Service
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class Msfs2024ToolsViewModel(
    val settingsRepository: SettingsRepository,
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
                        Locale.setDefault((l as Language).locale)
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
                )
            }

            is Msfs2024ToolsAction.OnSaveAirplanesClick -> {
                val newAirplanes = action.settings?.get<MutableList<String>>(SK.airplanes)
                newAirplanes?.removeIf { a -> a.isBlank() }
                saveSettings(
                    settings = action.settings?.copy(SK.airplanes, newAirplanes?.joinToString(","))
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
                    val project = ProjectConfiguration()
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
                        selectedTabLabel = action.tabLabels.firstOrNull()?.second,
                        isEditingSettings = false,
                        isShowInfos = false,
                        isEditingProjectConfiguration = false,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnTabSelected -> {
                _state.update { state ->
                    state.copy(
                        selectedTabIndex = action.index,
                        selectedTabLabel = state.tabLabels[action.index].second,
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

            is Msfs2024ToolsAction.OnCollapsibleStateChange -> {
                _state.update {
                    val newMap = it.collapsibleState.toMutableMap()
                    newMap[action.id] = action.isExpanded
                    it.copy(
                        collapsibleState = newMap
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
                        lm.log("MSFS")
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
        settingsRepository.getSettings()
            .onSuccess { (settings, projectConfigurations) ->
                Locale.setDefault(settings.get<Language>(SK.language)?.locale?: Language.EN.locale)
                _state.update {
                    it.copy(
                        settings = settings,
                        projectConfigurations = projectConfigurations,
                        isLoading = false,
                    )
                }
            }
            .onError { error, throwable ->
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
        conversion: Conversion?,
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
            .onError { error, throwable ->
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
        settings: Settings?
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

        _state.update {
            it.copy(
                isLoading = true,
            )
        }

        settingsRepository.saveSettings(settings)
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
            .onError { error, throwable ->
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
        if (projectConfiguration == null || projectConfiguration.get<String>(PK.airplaneName) == null || projectConfiguration.get<String>(
                PK.liveryName
            ) == null) {
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

        projectConfiguration.get<File>(PK.packageTextureDir)?.let { dir ->
            val result1 = settingsRepository.determineTextureFormat(dir)
            if (result1 is Result.Success) {
                var newConfiguration = projectConfiguration.copy(PK.textureFormatPackage, result1.data)
                newConfiguration.get<File>(PK.modelTexturesDir)?.let { dir ->
                    val result2 = settingsRepository.determineTextureFormat(dir)
                    if (result2 is Result.Success) {
                        newConfiguration = projectConfiguration.copy(PK.textureFormatModel, result2.data)
                        settingsRepository.saveProjectConfiguration(newConfiguration)
                            .onSuccess {
                                _state.update { state ->
                                    val projectConfigurations = state.projectConfigurations
                                        .filterNot { p ->
                                            (p.get<String>(PK.airplaneName) == newConfiguration.get<String>(PK.airplaneName) && p.get<String>(
                                                PK.liveryName
                                            ) == newConfiguration.get<String>(PK.liveryName))
                                                    || (p.get<String>(PK.airplaneName) == null && p.get<String>(PK.liveryName) == null)
                                        } + newConfiguration
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
                            .onError { error, throwable ->
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        uiMessage = error.toUiText(),
                                        uiMessageSeverity = Severity.Error
                                    )
                                }
                            }
                    } else if (result2 is Result.Error) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                uiMessage = result2.error.toUiText(),
                                uiMessageSeverity = Severity.Error
                            )
                        }
                    }
                }
            } else if (result1 is Result.Error) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        uiMessage = result1.error.toUiText(),
                        uiMessageSeverity = Severity.Error
                    )
                }
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

        settingsRepository.updateProjectConfigurations(projectConfigurations)
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
            .onError { error, throwable ->
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

        settingsRepository.deleteProjectConfiguration(projectConfiguration)
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
            .onError { error, throwable ->
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
