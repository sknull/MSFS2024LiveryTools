package de.visualdigits.msfs2024tools.presentation.model

import de.visualdigits.common.domain.model.Configuration
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.type.Conversion
import org.jetbrains.compose.resources.StringResource
import java.util.Locale

sealed interface Msfs2024ToolsAction {

    data class OnTabSelected(
        val index: Int,
        val label: StringResource?
    ): Msfs2024ToolsAction


    data class OnLanguageSelected(
        val locale: Locale,
    ): Msfs2024ToolsAction


    //
    // Global Configuration
    //

    class OnEditGlobalConfigurationClick : Msfs2024ToolsAction

    data class OnGlobalConfigurationValueChanged(
        val globalConfiguration: GlobalConfiguration?,
        val keyValue: KeyValue,
    ): Msfs2024ToolsAction

    class OnEditGlobalConfigurationCancelClick : Msfs2024ToolsAction

    data class OnSaveGlobalConfigurationClick(
        val globalConfiguration: GlobalConfiguration?,
        val projectConfigurations: List<ProjectConfiguration>
    ) : Msfs2024ToolsAction

    data class OnSaveAirplanesClick(
        val globalConfiguration: GlobalConfiguration?,
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


    data class OnPanelOkClick(
        val configuration: Configuration<*>?,
    ) : Msfs2024ToolsAction

    data class OnConversionClick(
        val globalConfiguration: GlobalConfiguration?,
        val currentProjectConfiguration: ProjectConfiguration,
        val conversion: Conversion,
        val dryRun: Boolean
    ): Msfs2024ToolsAction

    class OnBusyOkClick : Msfs2024ToolsAction
}
