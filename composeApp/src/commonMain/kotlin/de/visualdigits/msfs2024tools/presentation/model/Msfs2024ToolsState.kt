package de.visualdigits.msfs2024tools.presentation.model

import de.visualdigits.common.domain.model.UiText
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage
import org.jetbrains.compose.resources.StringResource


data class Msfs2024ToolsState(
    val settings: Settings? = null,
    val projectConfigurations: List<ProjectConfiguration> = listOf(),

    val originalSettings: Settings? = null,

    val originalProjectConfiguration: ProjectConfiguration? = null,
    val currentProjectConfiguration: ProjectConfiguration? = null,
    val isNewProject: Boolean = false,


    val selectedTabIndex: Int = 0,
    val selectedTabLabel: StringResource? = null,
    val tabLabels: List<StringResource> = listOf(),

    val isShowInfos: Boolean = false,
    val isEditingSettings: Boolean = false,
    val isEditingProjectConfiguration: Boolean = false,

    val isLoading: Boolean = false,
    val isConverting: Boolean = false,

    val errorMessage: UiText? = null,

    val currentProgress: Float = 0.0f,
    val logs: List<LogMessage> = listOf(),
)
