package de.visualdigits.msfs2024tools.presentation.model

import de.visualdigits.common.domain.model.UiText
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage


data class Msfs2024ToolsState(
    val globalConfiguration: GlobalConfiguration? = null,
    val projectConfigurations: List<ProjectConfiguration> = listOf(),

    val originalGlobalConfiguration: GlobalConfiguration? = null,

    val originalProjectConfiguration: ProjectConfiguration? = null,
    val currentProjectConfiguration: ProjectConfiguration? = null,
    val isNewProject: Boolean = false,


    val selectedTabIndex: Int = 0,
    val selectedTabLabel: String? = null,

    val isEditingGlobalConfiguration: Boolean = false,
    val isEditingProjectConfiguration: Boolean = false,

    val isLoading: Boolean = false,

    val errorMessage: UiText? = null,

    val currentProgress: Float = 0.0f,
    val logs: List<LogMessage> = listOf(),
)
