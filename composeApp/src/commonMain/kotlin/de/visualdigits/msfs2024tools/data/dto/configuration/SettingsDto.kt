package de.visualdigits.msfs2024tools.data.dto.configuration

import de.visualdigits.generated.AppVersion
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings.Companion.NVIDIA_TEXTURETOOL_PATH_DEFAULT
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings.Companion.SDK_ROOT_DEFAULT
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.domain.model.type.SimType
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Global model valid for all projects.
 */
@Serializable
data class SettingsDto(

    var version: String? = null,

    var language: Language? = Language.EN,

    /** Imported from imagetool - Simtype [MICROSFT, STEAM]. */
    var simType: SimType? = null,

    /** Imported from imagetool - Absolute path to the sdk (needed to convert png images to ktx2). */
    var sdkRoot: String = SDK_ROOT_DEFAULT,

    /** Imported from imagetool - Absolute path which to the layoutgenerator tool executable (needed to update layout.json) default is unset. */
    var layoutGeneratorToolPath: String? = null,

    /** Absolute path to the nvidia texture exporter executable (needed to convert ktx2 to png). */
    var nvidiaTextureToolPath: String = NVIDIA_TEXTURETOOL_PATH_DEFAULT,

    /** Root directory of the sim packages - used as starting directrory for dialogs. */
    var mainLibraryRootFolder: String? = null,

    /** Root directory of the livery projects - used as starting directrory for dialogs. */
    var projectRootFolder: String? = null,

    /** The known airplanes */
    val airplanes: MutableList<String> = mutableListOf(),

    /** List of configured project setups.  */
    val projects: MutableList<ProjectConfigurationDto> = mutableListOf()
) {

    companion object {

        val mapper = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        fun readValue(file: File): SettingsDto {
            return if (file.exists()) {
                try {
                    mapper.decodeFromString<SettingsDto>(file.readText())
                } catch (e: Exception) {
                    throw IllegalStateException("Could not parse file '$file'", e)
                }
            } else {
                SettingsDto()
            }
        }
    }

    fun clone(): SettingsDto {
        return SettingsDto(
            language = language,
            simType = simType,
            sdkRoot = sdkRoot,
            layoutGeneratorToolPath = layoutGeneratorToolPath,
            nvidiaTextureToolPath = nvidiaTextureToolPath,
            mainLibraryRootFolder = mainLibraryRootFolder,
            projectRootFolder = projectRootFolder,
            airplanes = airplanes.toMutableList(),
            projects = projects.toMutableList()
        )
    }

    fun addProject(
        airplaneName: String,
        liveryName: String,
        packageDir: String,
        packageTextureDir: File,
        modelTexturesDir: String,
        textureTypes: List<TextureType>? = null
    ) {
        val newProject = ProjectConfigurationDto(
            airplaneName = airplaneName,
            liveryName = liveryName,
            packageDir = packageDir,
            packageTextureDir = packageTextureDir.canonicalPath,
            modelTexturesDir = modelTexturesDir,
            textureTypes = textureTypes ?: TextureType.entries
        )
        projects.remove(newProject)
        projects.add(newProject)
    }

    operator fun get(airplaneName: String, liveryName: String): ProjectConfigurationDto? {
        return projects.find { p -> p.airplaneName == airplaneName && p.liveryName == liveryName }
    }
}
