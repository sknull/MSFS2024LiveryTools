package de.visualdigits.msfs2024tools.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.onError
import de.visualdigits.common.domain.model.onSuccess
import de.visualdigits.common.domain.util.toUiText
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage.Companion.log
import de.visualdigits.msfs2024tools.domain.model.type.Conversion
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

class Msfs2024ToolsViewModel(
    val configurationRepository: ConfigurationRepository,
    val msfs2024Service: Msfs2024Service,
) : ViewModel() {

    companion object {

        private const val MAX_LOG_LINES = 200
    }

    private val _state = MutableStateFlow(Msfs2024ToolsState())
    val state: StateFlow<Msfs2024ToolsState> = _state
        .onStart {
            if (_state.value.globalConfiguration == null ) {
                val (globalConfiguration, projectConfigurations) = configurationRepository.loadConfiguration()
                _state.update {
                    it.copy(
                        globalConfiguration = globalConfiguration,
                        projectConfigurations = projectConfigurations
                    )
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    init {
        Logger.i("Application initialized")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onAction(action: Msfs2024ToolsAction) {
        when (action) {
            //
            // Global Configuration
            //
            is Msfs2024ToolsAction.OnEditGlobalConfigurationClick -> {
                _state.update {
                    it.copy(
                        originalGlobalConfiguration = it.globalConfiguration,
                        isEditingGlobalConfiguration = true,
                        errorMessage = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnGlobalConfigurationValueChanged -> {
                _state.update {
                    val globalConfiguration = action.globalConfiguration?.copy(
                        key = action.keyValue.key,
                        value = action.keyValue.value
                    )
                    it.copy(
                        globalConfiguration = globalConfiguration,
                    )
                }
            }

            is Msfs2024ToolsAction.OnEditGlobalConfigurationCancelClick -> {
                _state.update {
                    it.copy(
                        globalConfiguration = it.originalGlobalConfiguration?.clone(),
                        isEditingGlobalConfiguration = false,
                        errorMessage = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnSaveGlobalConfigurationClick -> {
                saveGlobalConfiguration(
                    globalConfiguration = action.globalConfiguration,
                    projectConfigurations = action.projectConfigurations
                )
            }

            is Msfs2024ToolsAction.OnSaveAirplanesClick -> {
                action.globalConfiguration?.airplanes?.removeIf { a -> a.isBlank() }
                saveGlobalConfiguration(
                    globalConfiguration = action.globalConfiguration,
                    projectConfigurations = action.projectConfigurations
                )

                updateProjectConfigurations(action.projectConfigurations.filterNot { p -> p.airplaneName == null || p.liveryName == null })
            }


            //
            // Project Configuration
            //
            is Msfs2024ToolsAction.OnProjectClick -> {
                _state.update {
                    it.copy(
                        currentProjectConfiguration = action.projectConfiguration,
                        isEditingGlobalConfiguration = false,
                        isEditingProjectConfiguration = false,
                        errorMessage = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnNewProjectClick -> {
                _state.update {
                    val project = ProjectConfiguration(it.globalConfiguration)
                    it.copy(
                        originalProjectConfiguration = null,
                        currentProjectConfiguration = project,
                        projectConfigurations = it.projectConfigurations + project,
                        isEditingProjectConfiguration = true,
                        isNewProject = true,
                        errorMessage = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnEditProjectConfigurationClick -> {
                _state.update {
                    it.copy(
                        originalProjectConfiguration = it.currentProjectConfiguration?.clone(),
                        isEditingProjectConfiguration = true,
                        isNewProject = false,
                        errorMessage = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnProjectConfigurationValueChanged -> {
                _state.update {
                    it.copy(
                        currentProjectConfiguration = action.projectConfiguration?.copy(
                            key = action.keyValue.key,
                            value = action.keyValue.value
                        )
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
                        errorMessage = null
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
            is Msfs2024ToolsAction.OnTabSelected -> {
                _state.update {
                    it.copy(
                        selectedTabIndex = action.index,
                        selectedTabLabel = action.label,
                        isEditingGlobalConfiguration = false,
                        isEditingProjectConfiguration = false,
                        errorMessage = null
                    )
                }
            }

            is Msfs2024ToolsAction.OnPanelOkClick -> {
                _state.update {
                    it.copy(
                        currentProjectConfiguration = null,
                        isEditingGlobalConfiguration = false
                    )
                }
            }

            is Msfs2024ToolsAction.OnBusyOkClick -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        logs = listOf()
                    )
                }
            }

            is Msfs2024ToolsAction.OnConversionClick -> {
                executeConversion(
                    globalConfiguration = action.globalConfiguration?.clone(),
                    projectConfiguration = action.currentProjectConfiguration.clone(),
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

    private fun executeConversion(
        globalConfiguration: GlobalConfiguration?,
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
                isLoading = true,
            )
        }
        msfs2024Service.executeConversion(
            configuration = globalConfiguration,
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
                        errorMessage = null
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        currentProgress = 0.0f,
                        errorMessage = error.toUiText()
                    )
                }
            }
    }

    private fun saveGlobalConfiguration(
        globalConfiguration: GlobalConfiguration?,
        projectConfigurations: List<ProjectConfiguration>
    ) = viewModelScope.launch {
        if (globalConfiguration == null || globalConfiguration.simType == null) {
            _state.update {
                it.copy(
                    errorMessage = UiText.StringResourceId(Res.string.error_global_configuration_invalid),
                )
            }

            return@launch
        }

        // update globalConfiguration in all projects
        projectConfigurations.forEach { p ->
            p.globalConfiguration = globalConfiguration
        }

        _state.update {
            it.copy(
                isLoading = true,
            )
        }

        configurationRepository.saveGlobalConfiguration(globalConfiguration)
            .onSuccess {
                _state.update {
                    it.copy(
                        globalConfiguration = globalConfiguration,
                        isLoading = false,
                        isEditingGlobalConfiguration = false,
                        errorMessage = null
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }
    }

    private fun saveProjectConfiguration(
        projectConfiguration: ProjectConfiguration?,
    ) = viewModelScope.launch {
        if (projectConfiguration == null || projectConfiguration.airplaneName == null || projectConfiguration.liveryName == null) {
            _state.update {
                it.copy(
                    errorMessage = UiText.StringResourceId(Res.string.error_project_configuration_invalid),
                )
            }

            return@launch
        }

        _state.update {
            it.copy(
                isLoading = true,
            )
        }

        configurationRepository.saveProjectConfiguration(projectConfiguration)
            .onSuccess {
                _state.update { state ->
                    val projectConfigurations1 = state.projectConfigurations
                    val filterNot = projectConfigurations1
                        .filterNot { p ->
                            (p.airplaneName == projectConfiguration.airplaneName && p.liveryName == projectConfiguration.liveryName)
                                    || (p.airplaneName == null && p.liveryName == null)
                        }
                    val projectConfigurations = filterNot + projectConfiguration
                    state.copy(
                        currentProjectConfiguration = projectConfiguration,
                        projectConfigurations = projectConfigurations,
                        isLoading = false,
                        isEditingProjectConfiguration = false,
                        errorMessage = null
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toUiText()
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
                        errorMessage = null
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toUiText()
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
                        errorMessage = null,
                        isEditingGlobalConfiguration = false,
                        isEditingProjectConfiguration = false,
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }
    }
}
