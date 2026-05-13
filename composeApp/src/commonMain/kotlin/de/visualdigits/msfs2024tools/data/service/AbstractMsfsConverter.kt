package de.visualdigits.msfs2024tools.data.service

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.util.WindowsUtils.runCommand
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto
import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

abstract class AbstractMsfsConverter(
    protected val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MsfsConverter {

    protected fun checkDirectories(
        projectConfiguration: ProjectConfigurationDto,
        logger: (LogMessage) -> Unit
    ): Boolean {
        if (projectConfiguration.packageTextureDir == null) {
            logger(log(Severity.Error, "Package texture directory unset"))
        }
        if (projectConfiguration.modelTexturesDir == null) {
            logger(log(Severity.Error, "Model texture directory unset"))
        }
        if (projectConfiguration.packageTextureDir == null || projectConfiguration.modelTexturesDir == null) {
            return true
        }
        return false
    }

    suspend fun convertWithNvidiaTextureTool(
        settingsDto: SettingsDto,
        sourceDirectory: File,
        suffixesSource: List<String>,
        targetDirectory: File,
        suffixesTarget: List<String>,
        extensionToDrop: String,
        extensionToAdd: String = "",
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit,
        dryRun: Boolean = false
    ): List<File> = withContext(dispatcher) {
        val charsToDrop = extensionToDrop.length
        val modifiedFiles = determineModifiedFiles(
            sourceDirectory = sourceDirectory,
            sourceSuffixes = suffixesSource,
            targetDirectory = targetDirectory,
            targetSuffixes = suffixesTarget
        )
        val n = modifiedFiles.size.toFloat()
        if (n > 0) {
            modifiedFiles
                .forEachIndexed { i, f ->
                    progress(i / n)
                    logger(log(Severity.Info, "Converting texture file '${f.name}'"))
                    val targetFile = File(targetDirectory, "${f.name.dropLast(charsToDrop)}$extensionToAdd")
                    val command = listOf(
                        "\"${settingsDto.nvidiaTextureToolPath}\"",
                        "-o",
                        "\"${targetFile.canonicalPath}\"",
                        "\"${f.canonicalPath}\""
                    )
                    logger(log(Severity.Info, "Executing command '$command'"))
                    if (!dryRun) {
                        runCommand(
                            command = command,
                            workingDir = File(settingsDto.nvidiaTextureToolPath).parentFile?:error("No nvidia texture tool given"),
                            logger = logger,
                        )
                    } else {
                        logger(log(Severity.Warn, "Dry run - not actually executing command"))
                    }
                }
        } else {
            logger(log(Severity.Warn, "No modified textures found."))
        }

        modifiedFiles
    }

    /**
     * Determine texture files in the source folder which have been modified compared to the target folder.
     */
    fun determineModifiedFiles(
        sourceDirectory: File,
        sourceSuffixes: List<String>,
        targetDirectory: File,
        targetSuffixes: List<String>
    ): List<File> {
        val sourceFiles = sourceDirectory
            .listFiles { f -> f.isFile && sourceSuffixes.any { suffix -> f.name.endsWith(suffix, ignoreCase = true) } }
            .associate { f -> Pair(f.name.substringBefore("."), Pair(f.lastModified(), f)) }

        val targetFiles = targetDirectory
            .listFiles { f -> f.isFile && targetSuffixes.any { suffix -> f.name.endsWith(suffix, ignoreCase = true) } }
            .associate { f -> Pair(f.name.substringBefore("."), Pair(f.lastModified(), f)) }

        val files = sourceFiles.filter { (key, pair) ->
            !targetFiles.containsKey(key) || pair.first > (targetFiles[key]?.first ?: Long.MIN_VALUE)
        }.map { (_, pair) -> pair.second }

        return files
    }

    suspend fun generateLayoutJsonFile(
        settingsDto: SettingsDto,
        projectConfiguration: ProjectConfigurationDto,
        logger: (LogMessage) -> Unit,
    ) = withContext(dispatcher) {
        val layoutGeneratorToolPath = settingsDto.layoutGeneratorToolPath
        if (layoutGeneratorToolPath != null && File(layoutGeneratorToolPath).exists()) {
            logger(log(Severity.Info, "Generating ${projectConfiguration.packageDir}/layout.json"))
            val layoutFile = File(projectConfiguration.packageDir, "layout.json")
            runCommand(
                command = listOf(
                    "\"$layoutGeneratorToolPath\"",
                    "\"${layoutFile.canonicalPath}\""
                ),
                workingDir = File(layoutGeneratorToolPath),
                logger = logger,
            )
        } else {
            logger(log(Severity.Warn, "Layout generator executable not configured - not regenerating layout file!"))
        }
    }
}
