package de.visualdigits.msfs2024tools.data.service

import co.touchlab.kermit.Severity
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto
import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object DdsToPngConverter : AbstractMsfsConverter() {

    override suspend fun convert(
        settingsDto: SettingsDto,
        projectConfiguration: ProjectConfigurationDto,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit,
        dryRun: Boolean
    ): Boolean = withContext(Dispatchers.IO) {
        logger(log(Severity.Info, "Converting dds texture files in '${projectConfiguration.packageTextureDir}"))
        logger(log(Severity.Info, "Using target texture directory: ${projectConfiguration.modelTexturesDir}"))

        if (checkDirectories(projectConfiguration, logger)) return@withContext false

        val files = convertWithNvidiaTextureTool(
            settingsDto = settingsDto,
            sourceDirectory = projectConfiguration.packageTextureDir?.let { f -> File(f) }?:error("No package texture directory"),
            suffixesSource = projectConfiguration.textureTypes.map { tt -> "$tt.png.dds" },
            targetDirectory = projectConfiguration.modelTexturesDir?.let { f -> File(f) }?:error("No model texture directory"),
            suffixesTarget = listOf(".png"),
            extensionToDrop = ".dds",
            progress = progress,
            logger = logger,
            dryRun = dryRun,
        )

        files.isNotEmpty()
    }
}
