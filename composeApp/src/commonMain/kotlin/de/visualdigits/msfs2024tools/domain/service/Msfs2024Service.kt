package de.visualdigits.msfs2024tools.domain.service

import de.visualdigits.common.domain.model.Result
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage
import de.visualdigits.msfs2024tools.domain.model.type.Conversion

fun interface Msfs2024Service {

    suspend fun executeConversion(
        configuration: Settings?,
        project: ProjectConfiguration,
        conversion: Conversion,
        dryRun: Boolean,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit,
    ): Result<Unit, DataError.Local>
}
