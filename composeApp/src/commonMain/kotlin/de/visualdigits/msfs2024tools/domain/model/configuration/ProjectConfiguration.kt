package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.Configuration
import de.visualdigits.common.domain.model.Enumerable
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.label_airplaneName
import msfs2024liverytools.composeapp.generated.resources.label_liveryName
import msfs2024liverytools.composeapp.generated.resources.label_modelTexturesDir
import msfs2024liverytools.composeapp.generated.resources.label_packageDir
import msfs2024liverytools.composeapp.generated.resources.label_packageTextureDir
import msfs2024liverytools.composeapp.generated.resources.label_textureFormat
import msfs2024liverytools.composeapp.generated.resources.label_textureTypes
import msfs2024liverytools.composeapp.generated.resources.tooltip_airplaneName
import msfs2024liverytools.composeapp.generated.resources.tooltip_liveryName
import msfs2024liverytools.composeapp.generated.resources.tooltip_modelTexturesDir
import msfs2024liverytools.composeapp.generated.resources.tooltip_packageDir
import msfs2024liverytools.composeapp.generated.resources.tooltip_packageTextureDir
import msfs2024liverytools.composeapp.generated.resources.tooltip_textureFormat
import msfs2024liverytools.composeapp.generated.resources.tooltip_textureTypes
import org.jetbrains.compose.resources.StringResource
import java.io.File
import java.nio.file.Paths
import kotlin.reflect.KClass

data class ProjectConfiguration(

    var globalConfiguration: GlobalConfiguration?,

    var airplaneName: String? = null,

    var liveryName: String? = null,

    /** Absolute path to the directory containing the layout.json file for your target project. */
    var packageDir: File? = null,

    /** Absolute path which contains the textures in ktx2 format. */
    var packageTextureDir: File? = null,

    /** Absolute path which contains the textures in png format (i.e. the directory used by the blender model).  */
    var modelTexturesDir: File? = null,

    /** Determines with which texture flavor the project is working [KTX2, DDS] */
    var textureFormat: TextureFormat? = null,

    /** Optional coma separated list of texture type to process [ALBD,COMP,DECAL,NORM], default is all. */
    var textureTypes: List<TextureType> = TEXTURETYPES_DEFAULT,
): Configuration<ProjectConfiguration> {

    companion object {

        val TEXTURETYPES_DEFAULT = listOf(TextureType.ALBD, TextureType.COMP)
    }

    init {
        if (textureFormat == null) {
            val dir = packageTextureDir
            textureFormat = if (dir?.listFiles { file -> file.name.endsWith(".dds", ignoreCase = true) }?.isNotEmpty() == true) {
                TextureFormat.DDS
            } else if (dir?.listFiles { file -> file.name.endsWith(".ktx2", ignoreCase = true) }?.isNotEmpty() == true) {
                TextureFormat.KTX2
            } else {
                null
            }
        }
    }

    val thumbnailFile: File?
        get() {
            var thumbnailFile: File? = File(packageTextureDir, "thumbnail.png")
            if (thumbnailFile?.exists() == false) thumbnailFile = File(packageTextureDir, "thumbnail.jpg")
            if (thumbnailFile?.exists() == false) thumbnailFile = packageTextureDir?.let { d -> Paths.get(d.canonicalPath, "..", "thumbnail", "thumbnail.png").toFile() }
            if (thumbnailFile?.exists() == false) thumbnailFile = packageTextureDir?.let { d -> Paths.get(d.canonicalPath, "..", "thumbnail", "thumbnail.jpg").toFile() }

        return if (thumbnailFile?.exists() == true) thumbnailFile else null
    }

    override fun toString(): String {
        return asMap().toList().joinToString(", ") { e -> "${e.first}=\"${e.second}\"" }
    }

    override fun asMap(): Map<String, String?> {
        return mapOf(
            "airplaneName" to airplaneName,
            "liveryName" to liveryName,
            "packageDir" to packageDir?.canonicalPath,
            "packageTextureDir" to packageTextureDir?.canonicalPath,
            "modelTexturesDir" to modelTexturesDir?.canonicalPath,
            "textureFormat" to textureFormat?.name,
            "textureTypes" to textureTypes.joinToString(", "),
        )
    }

    override fun labelResource(id: String): StringResource? {
        return when(id) {
            "airplaneName" -> Res.string.label_airplaneName
            "liveryName" -> Res.string.label_liveryName
            "packageDir" -> Res.string.label_packageDir
            "packageTextureDir" -> Res.string.label_packageTextureDir
            "modelTexturesDir" -> Res.string.label_modelTexturesDir
            "textureFormat" -> Res.string.label_textureFormat
            "textureTypes" -> Res.string.label_textureTypes
            else -> null
        }
    }

    override fun toolTipResource(id: String): StringResource? {
        return when(id) {
            "airplaneName" -> Res.string.tooltip_airplaneName
            "liveryName" -> Res.string.tooltip_liveryName
            "packageDir" -> Res.string.tooltip_packageDir
            "packageTextureDir" -> Res.string.tooltip_packageTextureDir
            "modelTexturesDir" -> Res.string.tooltip_modelTexturesDir
            "textureFormat" -> Res.string.tooltip_textureFormat
            "textureTypes" -> Res.string.tooltip_textureTypes
            else -> null
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getFieldValues(key: String, clazz: KClass<T>): List<Pair<String, T>> {
        return when (key) {
            "airplaneName" -> globalConfiguration?.airplanes?.sorted()?.map { a -> Pair(a, a) } as List<Pair<String, T>>
            "textureFormat" -> TextureFormat.entries.map { e -> Pair(e.name, e.name) } as List<Pair<String, T>>
            "textureTypes" -> TextureType.entries.map { e -> Pair(e.name, e.name) }.sortedBy { e -> e.first } as List<Pair<String, T>>
            "thumbnailFile" -> listOf(Pair("jpg", "jpg"), Pair("png", "png")) as List<Pair<String, T>>
            else -> listOf<Pair<String, T>>()
        }
    }

    override fun fileMode(key: String): FileMode? {
        return when(key) {
            "packageDir" -> FileMode.DIRECTORIES_ONLY
            "packageTextureDir" -> FileMode.DIRECTORIES_ONLY
            "modelTexturesDir" -> FileMode.DIRECTORIES_ONLY
            "thumbnailFile" -> FileMode.FILES_ONLY
            else -> null
        }
    }

    override fun valid(key: String): Boolean? {
        return when (key) {
            "airplaneName" -> airplaneName?.isNotBlank() == true
            "liveryName" -> liveryName?.isNotBlank() == true
            "packageDir" -> packageDir?.exists() == true && packageDir?.isDirectory == true
            "packageTextureDir" -> packageTextureDir?.exists() == true && packageTextureDir?.isDirectory == true
            "modelTexturesDir" -> modelTexturesDir?.exists() == true && modelTexturesDir?.isDirectory == true
            "textureFormat" -> true // can be null for new projects
            "textureTypes" -> textureTypes.isNotEmpty()
            else -> null
        }
    }

    override fun fieldIsEditable(key: String): Boolean {
        return when (key) {
            "textureFormat" -> false // is calculated automatically from texture directory
            else -> true
        }
    }

    override fun clone(): ProjectConfiguration {
        return ProjectConfiguration(
            globalConfiguration,
            airplaneName,
            liveryName,
            packageDir,
            packageTextureDir,
            modelTexturesDir,
            textureFormat,
            textureTypes.toList(),
        )
    }

    override fun update(other: ProjectConfiguration): ProjectConfiguration {
        return ProjectConfiguration(
            other.globalConfiguration,
            airplaneName?:other.airplaneName,
            liveryName?:other.liveryName,
            packageDir?:other.packageDir,
            packageTextureDir?:other.packageTextureDir,
            modelTexturesDir?:other.modelTexturesDir,
            textureFormat?:other.textureFormat,
            other.textureTypes.toList(),
        )
    }

    override operator fun set(key: String, value: String): ProjectConfiguration {
        when (key) {
            "airplaneName" -> airplaneName = value
            "liveryName" -> liveryName = value
            "packageDir" -> packageDir = File(value)
            "packageTextureDir" -> packageTextureDir = File(value)
            "modelTexturesDir" -> modelTexturesDir = File(value)
            "textureFormat" -> textureFormat = TextureFormat.valueOf(value)
            "textureTypes" -> textureTypes = value.split(",").map { v -> TextureType.valueOf(v.trim()) }
        }

        return this
    }

    override fun copy(key: String?, value: String?): ProjectConfiguration {
        val newConfiguration = clone()
        when (key) {
            "airplaneName" -> newConfiguration.airplaneName = value?:""
            "liveryName" -> newConfiguration.liveryName = value?:""
            "packageDir" -> newConfiguration.packageDir = value?.let { v -> File(v) }
            "packageTextureDir" -> newConfiguration.packageTextureDir = value?.let { v -> File(v) }
            "modelTexturesDir" -> newConfiguration.modelTexturesDir = value?.let { v -> File(v) }
            "textureFormat" -> newConfiguration.textureFormat = value?.let { v -> TextureFormat.valueOf(v) }
            "textureTypes" -> newConfiguration.textureTypes = value?.split(",")?.map { v -> TextureType.valueOf(v.trim()) }?:listOf()
        }

        return newConfiguration
    }


    override fun fieldClass(key: String): Pair<Class<*>, Class<*>?>? {
        return when (key) {
            "airplaneName" -> Pair(Enumerable::class.java, null)
            "liveryName" -> Pair(String::class.java, null)
            "packageDir" -> Pair(File::class.java, null)
            "packageTextureDir" -> Pair(File::class.java, null)
            "modelTexturesDir" -> Pair(File::class.java, null)
            "textureFormat" -> Pair(TextureFormat::class.java, null)
            "textureTypes" -> Pair(TextureType::class.java, List::class.java)
            "thumbnailFile" -> Pair(File::class.java, null)
            else -> null
        }
    }

    override fun startDirectory(key: String): File {
        val rootFolder = when (key) {
            "packageDir" -> globalConfiguration?.mainLibraryRootFolder
            "packageTextureDir" -> packageDir ?: globalConfiguration?.mainLibraryRootFolder
            "modelTexturesDir" -> globalConfiguration?.projectRootFolder
            else -> null
        }
        return rootFolder ?: File(System.getProperty("user.home"))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProjectConfiguration

        if (globalConfiguration != other.globalConfiguration) return false
        if (airplaneName != other.airplaneName) return false
        if (liveryName != other.liveryName) return false
        if (packageDir != other.packageDir) return false
        if (packageTextureDir != other.packageTextureDir) return false
        if (modelTexturesDir != other.modelTexturesDir) return false
        if (textureFormat != other.textureFormat) return false
        if (textureTypes != other.textureTypes) return false
        if (thumbnailFile != other.thumbnailFile) return false

        return true
    }

    override fun hashCode(): Int {
        var result = globalConfiguration?.hashCode() ?: 0
        result = 31 * result + (airplaneName?.hashCode() ?: 0)
        result = 31 * result + (liveryName?.hashCode() ?: 0)
        result = 31 * result + (packageDir?.hashCode() ?: 0)
        result = 31 * result + (packageTextureDir?.hashCode() ?: 0)
        result = 31 * result + (modelTexturesDir?.hashCode() ?: 0)
        result = 31 * result + (textureFormat?.hashCode() ?: 0)
        result = 31 * result + textureTypes.hashCode()
        return result
    }
}
