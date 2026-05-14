package de.visualdigits.msfs2024tools.data.service

import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.msfs2024tools.data.model.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.model.configuration.SettingsDto

interface MsfsConverter {

    suspend fun convert(
        settingsDto: SettingsDto,
        projectConfiguration: ProjectConfigurationDto,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit = { },
        dryRun: Boolean = false
    ): Boolean
}
