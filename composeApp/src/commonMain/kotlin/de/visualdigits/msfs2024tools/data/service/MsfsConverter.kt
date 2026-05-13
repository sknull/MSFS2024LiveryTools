package de.visualdigits.msfs2024tools.data.service

import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto
import de.visualdigits.common.domain.model.errorhandling.LogMessage

interface MsfsConverter {

    suspend fun convert(
        settingsDto: SettingsDto,
        projectConfiguration: ProjectConfigurationDto,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit = { },
        dryRun: Boolean = false
    ): Boolean
}
