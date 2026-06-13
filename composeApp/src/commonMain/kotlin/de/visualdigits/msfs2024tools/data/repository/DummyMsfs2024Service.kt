package de.visualdigits.msfs2024tools.data.repository

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.logMessage
import de.visualdigits.common.domain.model.errorhandling.Result
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.model.type.Conversion
import de.visualdigits.msfs2024tools.domain.service.Msfs2024Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class DummyMsfs2024Service: Msfs2024Service {

    override suspend fun executeConversion(
        configuration: Settings?,
        project: ProjectConfiguration,
        conversion: Conversion?,
        dryRun: Boolean,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit
    ): Result<Unit, DataError.Local> = withContext(Dispatchers.IO) {
        if(configuration != null) {
            logger(logMessage(Severity.Info, "#### TEST - [dryRun=$dryRun] converting $conversion for project '${project.get<String>(
                PK.airplaneName)}_${project.get<String>(PK.liveryName)}' ####"))
            logger(logMessage(Severity.Assert, "Test Assert"))
            logger(logMessage(Severity.Verbose, "Test Verbose"))
            logger(logMessage(Severity.Debug, "Test Debug"))
            logger(logMessage(Severity.Warn, "Test Warn"))
            logger(logMessage(Severity.Error, "Test Error"))
            (0 until 100).forEach { i ->
                progress(0.01f * i)
                logger(logMessage(Severity.Info, "$i/100"))
                delay(100)
            }
            logger(logMessage(Severity.Info, "#### conversion finished ####"))
            Result.Success(Unit)
        } else {
            Result.Error(DataError.Local.SERIALIZATION)
        }
    }
}
