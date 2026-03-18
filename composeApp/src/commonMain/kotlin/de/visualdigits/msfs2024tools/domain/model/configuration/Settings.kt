package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.Configuration
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.domain.model.type.SimType
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.label_airplanes
import msfs2024liverytools.composeapp.generated.resources.label_language
import msfs2024liverytools.composeapp.generated.resources.label_layoutGeneratorToolPath
import msfs2024liverytools.composeapp.generated.resources.label_mainLibraryRootFolder
import msfs2024liverytools.composeapp.generated.resources.label_nvidiaTextureToolPath
import msfs2024liverytools.composeapp.generated.resources.label_projectRootFolder
import msfs2024liverytools.composeapp.generated.resources.label_sdkRoot
import msfs2024liverytools.composeapp.generated.resources.label_simType
import msfs2024liverytools.composeapp.generated.resources.tooltip_airplanes
import msfs2024liverytools.composeapp.generated.resources.tooltip_layoutGeneratorToolPath
import msfs2024liverytools.composeapp.generated.resources.tooltip_mainLibraryRootFolder
import msfs2024liverytools.composeapp.generated.resources.tooltip_nvidiaTextureToolPath
import msfs2024liverytools.composeapp.generated.resources.tooltip_projectRootFolder
import msfs2024liverytools.composeapp.generated.resources.tooltip_sdkRoot
import msfs2024liverytools.composeapp.generated.resources.tooltip_simType
import org.jetbrains.compose.resources.StringResource
import java.io.File
import kotlin.reflect.KClass

data class Settings(

    var language: Language? = Language.EN,

    /** Imported from imagetool - Simtype [MICROSFT, STEAM], default is MICROSOFT. */
    var simType: SimType? = null,

    /** Imported from imagetool - Absolute path to the sdk (needed to convert png images to ktx2) default is 'C:/MSFS 2024 SDK'. */
    var sdkRoot: File = File(SDK_ROOT_DEFAULT),

    /** Imported from imagetool - Absolute path which contains layoutgenerator tool (needed to update layout.json) default is unset. */
    var layoutGeneratorToolPath: File? = null,

    /** Absolute path to the nvidia texture exporter (needed to convert ktx2 to png), default is 'C:/Program Files/NVIDIA Corporation/NVIDIA Texture Tools/nvtt_export.exe' */
    var nvidiaTextureToolPath: File = File(NVIDIA_TEXTURETOOL_PATH_DEFAULT),

    /** Root directory of the sim packages - used as starting directrory for dialogs. */
    var mainLibraryRootFolder: File? = null,

    /** Root directory of the livery projects - used as starting directrory for dialogs. */
    var projectRootFolder: File? = null,

    /** The known airplanes */
    val airplanes: MutableList<String> = mutableListOf(),
): Configuration<Settings> {

    companion object {

        const val SDK_ROOT_DEFAULT = "C:/MSFS 2024 SDK"

        const val NVIDIA_TEXTURETOOL_PATH_DEFAULT = "C:/Program Files/NVIDIA Corporation/NVIDIA Texture Tools/nvtt_export.exe"
    }

    override fun toString(): String {
        return asMap().toList().joinToString(", ") { e -> "${e.first}=\"${e.second}\"" }
    }

    override fun clone(): Settings {
        return Settings(
            language,
            simType,
            sdkRoot,
            layoutGeneratorToolPath,
            nvidiaTextureToolPath,
            mainLibraryRootFolder,
            projectRootFolder,
            airplanes.toMutableList()
        )
    }

    override fun update(other: Settings): Settings {
        return Settings(
            language,
            simType?:other.simType,
            other.sdkRoot,
            layoutGeneratorToolPath?:other.layoutGeneratorToolPath,
            other.nvidiaTextureToolPath,
            mainLibraryRootFolder?:other.mainLibraryRootFolder,
            projectRootFolder?:other.projectRootFolder,
            other.airplanes.toMutableList()
        )
    }

    override fun asMap(): Map<String, String?> {
        return mapOf(
            "language" to language?.name,
            "simType" to simType?.name,
            "sdkRoot" to sdkRoot.canonicalPath,
            "layoutGeneratorToolPath" to layoutGeneratorToolPath?.canonicalPath,
            "nvidiaTextureToolPath" to nvidiaTextureToolPath.canonicalPath,
            "mainLibraryRootFolder" to mainLibraryRootFolder?.canonicalPath,
            "projectRootFolder" to projectRootFolder?.canonicalPath,
        )
    }

    override fun labelResource(id: String): StringResource? {
        return when (id) {
            "language" -> Res.string.label_language
            "simType" -> Res.string.label_simType
            "sdkRoot" -> Res.string.label_sdkRoot
            "layoutGeneratorToolPath" -> Res.string.label_layoutGeneratorToolPath
            "nvidiaTextureToolPath" -> Res.string.label_nvidiaTextureToolPath
            "mainLibraryRootFolder" -> Res.string.label_mainLibraryRootFolder
            "projectRootFolder" -> Res.string.label_projectRootFolder
            "airplanes" -> Res.string.label_airplanes
            else -> null
        }
    }

    override fun toolTipResource(id: String): StringResource? {
        return when (id) {
            "language" -> Res.string.label_language
            "simType" -> Res.string.tooltip_simType
            "sdkRoot" -> Res.string.tooltip_sdkRoot
            "layoutGeneratorToolPath" -> Res.string.tooltip_layoutGeneratorToolPath
            "nvidiaTextureToolPath" -> Res.string.tooltip_nvidiaTextureToolPath
            "mainLibraryRootFolder" -> Res.string.tooltip_mainLibraryRootFolder
            "projectRootFolder" -> Res.string.tooltip_projectRootFolder
            "airplanes" -> Res.string.tooltip_airplanes
            else -> null
        }
    }

    override fun copy(key: String?, value: String?): Settings {
        val newConfiguration = clone()
        when (key) {
            "language" -> newConfiguration.language = value?.let { v -> Language.valueOf(v)}
            "simType" -> newConfiguration.simType = value?.let { v -> SimType.valueOf(v) }
            "sdkRoot" -> newConfiguration.sdkRoot = value?.let { v -> File(v) }?:File(SDK_ROOT_DEFAULT)
            "layoutGeneratorToolPath" -> newConfiguration.layoutGeneratorToolPath = value?.let { v -> File(v) }
            "nvidiaTextureToolPath" -> newConfiguration.nvidiaTextureToolPath = value?.let { v -> File(v) }?:File(NVIDIA_TEXTURETOOL_PATH_DEFAULT)
            "mainLibraryRootFolder" -> newConfiguration.mainLibraryRootFolder = value?.let { v -> File(v) }
            "projectRootFolder" -> newConfiguration.projectRootFolder = value?.let { v -> File(v) }
            "airplanes" -> {
                newConfiguration.airplanes.clear()
                val map = value?.split(",")?.map { v -> v.trim() }
                newConfiguration.airplanes.addAll(map ?:mutableListOf())
            }
        }
        return newConfiguration
    }

    override fun fieldClass(key: String): Pair<Class<*>, Class<*>?>? {
        return when (key) {
            "language" -> Pair(Language::class.java, null)
            "simType" -> Pair(SimType::class.java, null)
            "sdkRoot" -> Pair(File::class.java, null)
            "layoutGeneratorToolPath" -> Pair(File::class.java, null)
            "nvidiaTextureToolPath" -> Pair(File::class.java, null)
            "mainLibraryRootFolder" -> Pair(File::class.java, null)
            "projectRootFolder" -> Pair(File::class.java, null)
            "airplanes" -> Pair(String::class.java, List::class.java)
            else -> null
        }
    }

    override fun startDirectory(key: String): File {
        val rootFolder = when (key) {
            "mainLibraryRootFolder" -> mainLibraryRootFolder
            "projectRootFolder" -> projectRootFolder
            else -> null
        }
        return rootFolder ?: File(System.getProperty("user.home"))
    }

    @Suppress("UNCHECKED_CAST")
    override fun <S : Any, D : Any> getFieldValues(key: String, sKlass: KClass<S>, dClass: KClass<D>): List<Triple<String, S, D?>?> {
        return when (key) {
            "language" -> Language.entries.map { e -> Triple(e.name, e.stringResourceId, e.drawableResourceId) }.sortedBy { e -> e.first } as List<Triple<String, S, D>>
            "simType" -> SimType.entries.map { e -> Triple(e.name, e.name, null) }.sortedBy { e -> e.first } as List<Triple<String, S, D>>
            "layoutGeneratorToolPath" -> listOf(Triple("exe", "exe", null)) as List<Triple<String, S, D>>
            "nvidiaTextureToolPath" -> listOf(Triple("exe", "exe", null)) as List<Triple<String, S, D>>
            "airplanes" -> airplanes.sorted().map { a -> Triple(a, a, null) } as List<Triple<String, S, D>>
            "airplaneName" -> airplanes.sorted().map { a -> Triple(a, a, null) } as List<Triple<String, S, D>> // needed for airplanes tab
            else -> listOf<Triple<String, S, D>>()
        }
    }

    override fun fileMode(key: String): FileMode? {
        return when (key) {
            "sdkRoot" -> FileMode.DIRECTORIES_ONLY
            "layoutGeneratorToolPath" -> FileMode.FILES_ONLY
            "nvidiaTextureToolPath" -> FileMode.FILES_ONLY
            "imageToMSFSKTX2Path" -> FileMode.DIRECTORIES_ONLY
            "mainLibraryRootFolder" -> FileMode.DIRECTORIES_ONLY
            "projectRootFolder" -> FileMode.DIRECTORIES_ONLY
            else -> null
        }
    }

    override fun valid(key: String): Boolean? {
        return when (key) {
            "language" -> language != null
            "simType" -> simType != null
            "sdkRoot" -> File(sdkRoot, "Tools").exists() && sdkRoot.isDirectory
            "layoutGeneratorToolPath" -> layoutGeneratorToolPath == null || (layoutGeneratorToolPath?.exists() == true && layoutGeneratorToolPath?.isFile == true)
            "nvidiaTextureToolPath" -> nvidiaTextureToolPath.exists() && nvidiaTextureToolPath.isFile()
            "mainLibraryRootFolder" -> true // okay to be unset
            "projectRootFolder" -> true // okay to be unset
            "airplanes" -> true // okay to be empty
            else -> null
        }
    }

    override fun fieldIsEditable(key: String): Boolean {
        return true
    }
}
