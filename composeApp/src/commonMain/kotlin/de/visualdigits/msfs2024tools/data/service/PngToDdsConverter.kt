package de.visualdigits.msfs2024tools.data.service

import co.touchlab.kermit.Severity
import de.visualdigits.msfs2024tools.data.dto.configuration.GlobalConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage.Companion.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object PngToDdsConverter : AbstractMsfsConverter() {

    override suspend fun convert(
        globalConfiguration: GlobalConfigurationDto,
        projectConfiguration: ProjectConfigurationDto,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit,
        dryRun: Boolean
    ): Boolean = withContext(Dispatchers.IO) {
        logger(log(Severity.Info, "Converting png texture files in '${projectConfiguration.packageTextureDir}'"))
        logger(log(Severity.Info, "Using target texture directory: ${projectConfiguration.modelTexturesDir}"))

        if (checkDirectories(projectConfiguration, logger)) return@withContext false

        val files = convertWithNvidiaTextureTool(
            globalConfiguration = globalConfiguration,
            sourceDirectory = File(projectConfiguration.modelTexturesDir),
            suffixesSource = projectConfiguration.textureTypes.map { tt -> "$tt.png" },
            targetDirectory = File(projectConfiguration.packageTextureDir),
            suffixesTarget = listOf(".dds"),
            extensionToDrop = ".png",
            extensionToAdd = ".PNG.DDS",
            progress = progress,
            logger = logger,
            dryRun = dryRun
        )

        files.isNotEmpty()
    }
}
