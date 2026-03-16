package de.visualdigits.msfs2024tools.data.service

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.util.WindowsUtils
import de.visualdigits.common.domain.util.WindowsUtils.runCommand
import de.visualdigits.common.domain.util.copyToIfNotExists
import de.visualdigits.common.domain.util.createDirectoryIfNotExists
import de.visualdigits.common.domain.util.writeValueAsXmlFile
import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.msfs2024.Project
import de.visualdigits.msfs2024tools.data.dto.msfs2024.assetpackage.AssetPackage
import de.visualdigits.msfs2024tools.data.dto.msfs2024.descriptor.BitmapConfiguration
import de.visualdigits.msfs2024tools.data.dto.msfs2024.usersettings.UserSettings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage.Companion.log
import de.visualdigits.msfs2024tools.domain.model.type.SimType
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

object PngToKtx2Converter : AbstractMsfsConverter() {

    /**
     * Converts all png textures in the given sourceDir to ktx2.
     */
    override suspend fun convert(
        settingsDto: SettingsDto,
        projectConfiguration: ProjectConfigurationDto,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit,
        dryRun: Boolean
    ): Boolean = withContext(Dispatchers.IO) {
        logger(log(Severity.Info, "Converting png texture files in '${projectConfiguration.modelTexturesDir}'"))
        logger(log(Severity.Info, "Using target texture directory: ${projectConfiguration.packageTextureDir}"))

        if (checkDirectories(projectConfiguration, logger)) return@withContext false

        val tempDir = File(projectConfiguration.modelTexturesDir, "TempPackage")
            .createDirectoryIfNotExists(
                logger = logger,
            )
        prepareTempPackage(
            tempDir = tempDir,
            logger = logger,
        )
        val modifiedFiles = preprocessTextures(
            projectConfiguration = projectConfiguration,
            logger = logger,
            dryRun = dryRun
        )
        if (!dryRun) {
            if (modifiedFiles.isNotEmpty()) {
                runPackageTool(
                    settingsDto = settingsDto,
                    projectConfiguration = projectConfiguration,
                    progress = progress,
                    logger = logger,
                    numberOfFiles = modifiedFiles.size.toFloat(),
                    tempDir = tempDir
                )
                collectConvertedTextures(
                    projectConfiguration = projectConfiguration,
                    logger = logger,
                )
            } else {
                logger(log(Severity.Warn, "No modified textures found."))
            }
        } else {
            logger(log(Severity.Info, "No actual conversion done due to a dry run."))
        }

        logger(log(Severity.Info, "Deleting temporary directory."))
        tempDir.deleteRecursively()

        if (!dryRun && modifiedFiles.isNotEmpty()) {
            generateLayoutJsonFile(
                settingsDto = settingsDto,
                projectConfiguration = projectConfiguration,
                logger = logger,
            )
        } else {
            logger(log(Severity.Warn, "No images converted - not regenerating layout.json"))
        }

        modifiedFiles.isNotEmpty()
    }

    private suspend fun prepareTempPackage(
        tempDir: File,
        logger: (LogMessage) -> Unit,
    ) = withContext(Dispatchers.IO) {
        val packageDefinitionsDir = File(tempDir, "PackageDefinitions").createDirectoryIfNotExists(
            logger = logger,
        )
        AssetPackage.ASSET_PACKAGE_DEFAULT.writeValueAsXmlFile(File(packageDefinitionsDir, "png-2-ktx2.xml"), indent = false, writeXmlDeclaration = false)
        Project.PROJECT_DEFAULT.writeValueAsXmlFile(File(tempDir, "png-2-ktx2.xml"), indent = false)
        UserSettings.USER_SETTINGS_DEFAULT.writeValueAsXmlFile(File(tempDir, "png-2-ktx2.xml.user"), indent = false, writeXmlDeclaration = false)
    }

    /**
     * Copies all png texture files in the given sourceDir along with the
     * appropriate descriptor file for the texture type to the package directory.
     *
     * Returns true if textures have been modified and need to be converted, false otherwise.
     */
    private suspend fun preprocessTextures(
        projectConfiguration: ProjectConfigurationDto,
        logger: (LogMessage) -> Unit,
        dryRun: Boolean = false
    ): List<File> = withContext(Dispatchers.IO) {
        logger(log(Severity.Info, "Preprocessing source directory '${projectConfiguration.modelTexturesDir}'..."))

        val tempTargetDir = projectConfiguration.modelTexturesDir?.let { d -> File(d) }
                ?.resolve("TempPackage/PackageSources/SimObjects/Airplanes/png-2-ktx2/common/texture")
                ?.createDirectoryIfNotExists(
                    logger = logger,
                )
        val modifiedFiles = determineModifiedFiles(
            sourceDirectory = File(projectConfiguration.modelTexturesDir),
            sourceSuffixes = projectConfiguration.textureTypes.map { tt -> "$tt.png" },
            targetDirectory = File(projectConfiguration.packageTextureDir),
            targetSuffixes = listOf(".ktx2")
        )
        modifiedFiles
            .forEachIndexed { _, f ->
                val textureType = determineTextureType(f)
                val targetFile = File(tempTargetDir, f.name)
                if (!targetFile.exists()) {
                    logger(log(Severity.Info, "Copy texture file '${f.name}'"))
                    if (!dryRun) {
                        f.copyToIfNotExists(
                            targetFile = targetFile,
                            logger = logger,
                        )
                    }
                }
                logger(log(Severity.Info, "Generating descriptor file '${f.name}.xml'"))
                if (!dryRun) {
                    val bitmapConfiguration =
                        BitmapConfiguration(
                            textureType = textureType
                        )
                    bitmapConfiguration.writeValueAsXmlFile(File(tempTargetDir, "${f.name}.xml"), indent = false)
                }
            }

        modifiedFiles
    }

    private suspend fun runPackageTool(
        settingsDto: SettingsDto,
        projectConfiguration: ProjectConfigurationDto,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit = { },
        numberOfFiles: Float,
        tempDir: File
    ) = withContext(Dispatchers.IO) {
        val msfsPackageToolPath = Paths.get(settingsDto.sdkRoot,"Tools", "bin", "fspackagetool.exe").absolutePathString()
        check(File(msfsPackageToolPath).exists()) { "Package tool '$msfsPackageToolPath' does not exist - terminating" }

        val command = mutableListOf(
            "\"$msfsPackageToolPath\"",
            "-nopause",
            "-rebuild"
        )
        if (settingsDto.simType == SimType.STEAM){
            command.add("-forcesteam")
        }
        command.addAll(listOf(
            "-outputtoseparateconsole",
            "\"png-2-ktx2.xml\"",
        ))

        logger(log(Severity.Info, "Executing command '${command.joinToString(" ")}'..."))

        runCommand(
            command = command,
            workingDir = tempDir,
            logger = logger,
        )
        var tasks = WindowsUtils.getRunningTasks()
        val finalTexturesDir = File(projectConfiguration.modelTexturesDir)
            .resolve("TempPackage/Packages/png-2-ktx2/SimObjects/Airplanes/png-2-ktx2/common/texture")

        val detectedFiles = mutableSetOf<String>()
        while (tasks.any("Abbildname", "FlightSimulator2024.exe")) {
            tasks = WindowsUtils.getRunningTasks()
            val allFiles = finalTexturesDir
                .listFiles { f -> f.isFile && f.name.endsWith(".ktx2", ignoreCase = true) }
                ?:emptyArray<File>()
            val newFiles = allFiles.filter { f -> f.name !in detectedFiles }
            if (newFiles.isNotEmpty()) {
                detectedFiles.addAll(
                    newFiles.map { f ->
                        logger(log(Severity.Info, "Detected converted image '${f.name}'"))
                        f.name
                    }
                )
                progress(detectedFiles.size / numberOfFiles)
            }
            delay(100)
        }
        logger(log(Severity.Info, "Command finished."))
    }

    private suspend fun collectConvertedTextures(
        projectConfiguration: ProjectConfigurationDto,
        logger: (LogMessage) -> Unit,
    ) = withContext(Dispatchers.IO) {
        logger(log(Severity.Info, "Copy ktx2 textures with descriptors..."))
        val finalTexturesDir = File(projectConfiguration.modelTexturesDir)
            .resolve("TempPackage/Packages/png-2-ktx2/SimObjects/Airplanes/png-2-ktx2/common/texture")
        findFiles(finalTexturesDir, listOf(".ktx2", ".json"))
            .forEach { f -> f.copyTo(File(projectConfiguration.packageTextureDir, f.name), overwrite = true) }
    }

    fun findFiles(dir: File, suffixes: List<String>): List<File> {
        return if (dir.exists()) {
            dir.listFiles { f -> f.isFile && suffixes.any { s -> f.name.endsWith(s, ignoreCase = true) } }
                ?.toList()
                ?:listOf()
        } else {
            listOf()
        }
    }

    private fun determineTextureType(textureFile: File): TextureType {
        return when {
            textureFile.name.endsWith("_albd.png", ignoreCase = true) -> {
                TextureType.ALBD
            }
            textureFile.name.endsWith("_comp.png", ignoreCase = true) -> {
                TextureType.COMP
            }
            textureFile.name.endsWith("_decal.png", ignoreCase = true) -> {
                TextureType.DECAL
            }
            textureFile.name.endsWith("_norm.png", ignoreCase = true) -> {
                TextureType.NORM
            }
            else -> {
                error("Unsupoorted texture file: $textureFile")
            }
        }
    }
}
