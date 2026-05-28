package de.visualdigits.msfs2024tools.presentation.model

import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.type.Conversion
import de.visualdigits.msfs2024tools.domain.model.type.Language

sealed interface Msfs2024ToolsAction {

    data class OnTabSelected(
        val index: Int
    ): Msfs2024ToolsAction

    data class OnInitializeTabs(
        val tabLabels: List<Pair<String, UiText>>
    ): Msfs2024ToolsAction

    data class OnLanguageSelected(
        val language: Language,
    ): Msfs2024ToolsAction


    data class OnCollapsibleStateChange(
        val id: String,
        val isExpanded: Boolean
    ): Msfs2024ToolsAction

    //
    // Settings
    //

    data class OnEditSettingsClick(
        val isEditingSettings: Boolean
    ) : Msfs2024ToolsAction

    data class OnSettingsValueChanged(
        val settings: Settings?,
        val keyValue: KeyValue,
    ): Msfs2024ToolsAction

    class OnEditSettingsCancelClick : Msfs2024ToolsAction

    data class OnSaveSettingsClick(
        val settings: Settings?,
        val projectConfigurations: List<ProjectConfiguration>
    ) : Msfs2024ToolsAction

    data class OnSaveAirplanesClick(
        val settings: Settings?,
        val projectConfigurations: List<ProjectConfiguration>
    ) : Msfs2024ToolsAction


    //
    // Project Configuration
    //
    data class OnProjectClick(
        val projectConfiguration: ProjectConfiguration
    ): Msfs2024ToolsAction

    class OnNewProjectClick : Msfs2024ToolsAction

    class OnEditProjectConfigurationClick : Msfs2024ToolsAction

    data class OnProjectConfigurationValueChanged(
        val projectConfiguration: ProjectConfiguration?,
        val keyValue: KeyValue,
    ): Msfs2024ToolsAction

    class OnEditProjectConfigurationCancelClick(
        val originalProjectConfiguration: ProjectConfiguration?
    ) : Msfs2024ToolsAction

    data class OnSaveProjectConfigurationClick(
        val projectConfiguration: ProjectConfiguration?
    ) : Msfs2024ToolsAction

    data class OnDeleteProjectClick(
        val projectConfiguration: ProjectConfiguration
    ) : Msfs2024ToolsAction


    //
    //
    //
    data class OnShowInfosClick(
        val isShowInfos: Boolean
    ) : Msfs2024ToolsAction

    data class OnPanelOkClick(
        val configuration: AbstractConfiguration<*,*>?,
    ) : Msfs2024ToolsAction

    data class OnConversionClick(
        val settings: Settings?,
        val currentProjectConfiguration: ProjectConfiguration,
        val conversion: Conversion?,
        val dryRun: Boolean
    ): Msfs2024ToolsAction

    class OnBusyOkClick : Msfs2024ToolsAction
}
