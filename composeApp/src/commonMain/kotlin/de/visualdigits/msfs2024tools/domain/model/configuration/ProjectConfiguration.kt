package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.KeyFactory
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ReferenceListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.SpacerFieldDescriptor
import de.visualdigits.common.domain.model.configuration.StringFieldDescriptor
import de.visualdigits.common.domain.model.configuration.StringKeyFactory
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
import msfs2024liverytools.composeapp.generated.resources.tooltip_liveryName
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import java.io.File
import java.nio.file.Paths

@Suppress("UNCHECKED_CAST")
class ProjectConfiguration(
    fields: LinkedHashMap<PK, Field<*,*,PK>> = LinkedHashMap(),
    var settings: Settings?
): AbstractConfiguration<ProjectConfiguration, PK>(fields) {

    companion object {

        val TEXTURETYPES_DEFAULT = listOf(TextureType.ALBD, TextureType.COMP)
    }

    override fun setupFields(): List<Field<*,*,PK>> {
        return listOf(
            /** The airplane name of the project. */
            Field(
                descriptor = ReferenceListFieldDescriptor(
                    fieldClass = String::class,
                    key = PK.airplaneName,
                    label = Res.string.label_airplaneName,
                    keyFactory = StringKeyFactory,
                    options = {
                        settings?.get<List<String>>(SK.airplanes)
                            ?.sorted()
                            ?.map { airplaneName ->
                                Triple<String, StringResource?, DrawableResource?>(airplaneName, null, null)
                            }
                            ?:listOf() }
                ),
                valid = { value ->
                    (value as? String)?.isNotBlank() == true
                }
            ),

            /** The livery name of the project. */
            Field(
                descriptor = StringFieldDescriptor(
                    key = PK.liveryName,
                    label = Res.string.label_liveryName,
                    toolTip = Res.string.tooltip_liveryName
                ),
                valid = { value -> (value as? String)?.isNotBlank() == true }
            ),

            /** Absolute path to the directory containing the layout.json file for your target project. */
            Field(
                descriptor = FileFieldDescriptor(
                    key = PK.packageDir,
                    label = Res.string.label_packageDir,
                    fileMode = FileMode.DIRECTORIES_ONLY,
                ),
                valid = { value -> (value as? File)?.exists() == true && value.isDirectory }
            ),

            Field(descriptor = SpacerFieldDescriptor(PK.spacer)),

            /** Absolute path which contains the textures in ktx2 format. */
            Field(
                descriptor = FileFieldDescriptor(
                    key = PK.packageTextureDir,
                    label = Res.string.label_packageTextureDir,
                    fileMode = FileMode.DIRECTORIES_ONLY,
                    startDirectory = {configuration ->
                        val get = (configuration as? ProjectConfiguration)?.get<File>(PK.packageDir)
                        get ?:File(System.getProperty("user.home"))
                    }
                ),
                valid = { value -> (value as? File)?.exists() == true && value.isDirectory }
            ),

            /** Determines with which texture flavor the project is working [KTX2, DDS] */
            Field(
                descriptor = EnumFieldDescriptor(
                    fieldClass = TextureFormat::class,
                    key = PK.textureFormatPackage,
                    readOnly = true,
                    label = Res.string.label_textureFormat,
                    options = { TextureFormat.entries.map { e -> Triple(e.name, null, null) } },
                    keyFactory = TextureFormat
                ),
                valid = { value -> value != null }
            ),

            /** Absolute path which contains the textures in png format (i.e. the directory used by the blender model).  */
            Field(
                descriptor = FileFieldDescriptor(
                    key = PK.modelTexturesDir,
                    label = Res.string.label_modelTexturesDir,
                    fileMode = FileMode.DIRECTORIES_ONLY,
                ),
                valid = { value -> (value as? File)?.exists() == true && value.isDirectory }
            ),

            /** Determines with which texture flavor the project is working [KTX2, DDS] */
            Field(
                descriptor = EnumFieldDescriptor(
                    fieldClass = TextureFormat::class,
                    key = PK.textureFormatModel,
                    readOnly = true,
                    label = Res.string.label_textureFormat,
                    options = { TextureFormat.entries.map { e -> Triple(e.name, null, null) } },
                    keyFactory = TextureFormat
                ),
                valid = { value -> value != null }
            ),

            /** Optional coma separated list of texture type to process [ALBD,COMP,DECAL,NORM], default is all. */
            Field(
                descriptor = ListFieldDescriptor(
                    fieldClass = TextureType::class,
                    key = PK.textureTypes,
                    label = Res.string.label_textureTypes,
                    options = { TextureType.entries.map { e -> Triple(e.name, null, null) } },
                    keyFactory = TextureTypeListKeyFactory
                ),
                valid = { value -> (value as? MutableList<TextureType>)?.isNotEmpty() == true }
            ),
        )
    }

    val thumbnailFile: File?
        get() {
            val packageTextureDir = get<File>(PK.packageTextureDir)
            var thumbnailFile: File? = File(packageTextureDir, "thumbnail.png")
            if (thumbnailFile?.exists() == false) thumbnailFile = File(packageTextureDir, "thumbnail.jpg")
            if (thumbnailFile?.exists() == false) thumbnailFile = packageTextureDir?.let { d -> Paths.get(d.canonicalPath, "..", "thumbnail", "thumbnail.png").toFile() }
            if (thumbnailFile?.exists() == false) thumbnailFile = packageTextureDir?.let { d -> Paths.get(d.canonicalPath, "..", "thumbnail", "thumbnail.jpg").toFile() }

        return if (thumbnailFile?.exists() == true) thumbnailFile else null
    }

    override fun createInstance(newFields: LinkedHashMap<PK, Field<*,*,PK>>): ProjectConfiguration {
        return ProjectConfiguration(newFields, settings)
    }
}

class TextureTypeListKeyFactory {

    companion object : KeyFactory<MutableList<TextureType>> {

        override fun fromString(value: String?): MutableList<TextureType>  = value
            ?.split(",")
            ?.map { v -> v.trim() }
            ?.mapNotNull { v -> TextureType.fromString(v) }
            ?.toMutableList()
            ?:mutableListOf()

        override fun stringValue(value: Any?): String? {
            val s=  (value as? MutableList<TextureType>)?.joinToString(",") { v -> v.name }
            return s
        }
    }
}
