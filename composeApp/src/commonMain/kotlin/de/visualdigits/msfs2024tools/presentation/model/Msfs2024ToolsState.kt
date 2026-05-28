package de.visualdigits.msfs2024tools.presentation.model

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings


data class Msfs2024ToolsState(
    val settings: Settings? = null,
    val projectConfigurations: List<ProjectConfiguration> = listOf(),

    val originalSettings: Settings? = null,

    val originalProjectConfiguration: ProjectConfiguration? = null,
    val currentProjectConfiguration: ProjectConfiguration? = null,
    val isNewProject: Boolean = false,

    val selectedTabIndex: Int = 0,
    val selectedTabLabel: UiText? = null,
    val tabLabels: List<Pair<String, UiText>> = listOf(),

    val isShowInfos: Boolean = false,
    val isEditingSettings: Boolean = false,
    val isEditingProjectConfiguration: Boolean = false,

    val isLoading: Boolean = false,
    val isConverting: Boolean = false,

    val uiMessage: UiText? = null,
    val uiMessageSeverity: Severity? = null,

    val currentProgress: Float = 0.0f,
    val logs: List<LogMessage> = listOf(),

    val collapsibleState: MutableMap<String, Boolean> = mutableMapOf(),
)
