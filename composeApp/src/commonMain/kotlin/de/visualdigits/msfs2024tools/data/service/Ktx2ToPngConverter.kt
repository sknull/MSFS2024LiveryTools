package de.visualdigits.msfs2024tools.data.service

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.logMessage
import de.visualdigits.msfs2024tools.data.model.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.model.configuration.SettingsDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object Ktx2ToPngConverter : AbstractMsfsConverter() {

    override suspend fun convert(
        settingsDto: SettingsDto,
        projectConfiguration: ProjectConfigurationDto,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit,
        dryRun: Boolean
    ): Boolean = withContext(Dispatchers.IO) {
        logger(logMessage(Severity.Info, "Converting ktx2 texture files in '${projectConfiguration.packageTextureDir}'"))
        logger(logMessage(Severity.Info, "Using target texture directory: ${projectConfiguration.modelTexturesDir}"))

        if (checkDirectories(projectConfiguration, logger)) return@withContext false

        val files = convertWithNvidiaTextureTool(
            settingsDto = settingsDto,
            sourceDirectory = projectConfiguration.packageTextureDir?.let { f -> File(f) }?:error("No package texture directory"),
            suffixesSource = projectConfiguration.textureTypes.map { tt -> "$tt.png.ktx2" },
            targetDirectory = projectConfiguration.modelTexturesDir?.let { f -> File(f) }?:error("No model texture directory"),
            suffixesTarget = listOf(".png"),
            extensionToDrop = ".ktx2",
            progress = progress,
            logger = logger,
            dryRun = dryRun
        )

        files.isNotEmpty()
    }
}
