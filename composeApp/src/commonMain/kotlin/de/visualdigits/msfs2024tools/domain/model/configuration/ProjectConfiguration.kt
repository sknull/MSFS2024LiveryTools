package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.StringFieldDescriptor
import de.visualdigits.msfs2024tools.domain.model.type.Language
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
    fieldDescriptors: LinkedHashMap<String, AbstractFieldDescriptor<*,*,*>> = LinkedHashMap(),
    fields: LinkedHashMap<String, Field<*,*,*>> = LinkedHashMap(),
    var settings: Settings?
): AbstractConfiguration<ProjectConfiguration>(fieldDescriptors, fields) {

    companion object {

        val TEXTURETYPES_DEFAULT = listOf(TextureType.ALBD, TextureType.COMP)
    }

    override fun setupFieldDescriptors(): List<AbstractFieldDescriptor<*, *,*>> {
        return listOf(
            EnumFieldDescriptor(
                fieldClass = String::class,
                key = "airplaneName",
                label = Res.string.label_airplaneName,
                sorted = true,
                options = {
                    settings?.get<List<String>>("airplanes")
                        ?.sorted()
                        ?.map { airplaneName ->
                            Triple<String, StringResource?, DrawableResource?>(airplaneName, null, null)
                        }
                        ?:listOf() }
            ),
            StringFieldDescriptor(
                key = "liveryName",
                label = Res.string.label_liveryName,
                toolTip = Res.string.tooltip_liveryName
            ),

            /** Absolute path to the directory containing the layout.json file for your target project. */
            FileFieldDescriptor(
                key = "packageDir",
                label = Res.string.label_packageDir,
                fileMode = FileMode.DIRECTORIES_ONLY,
            ),

            /** Absolute path which contains the textures in ktx2 format. */
            FileFieldDescriptor(
                key = "packageTextureDir",
                label = Res.string.label_packageTextureDir,
                fileMode = FileMode.DIRECTORIES_ONLY,
                startDirectory = { this@ProjectConfiguration.get<File>("packageDir")?:File(System.getProperty("user.home")) }
            ),

            /** Absolute path which contains the textures in png format (i.e. the directory used by the blender model).  */
            FileFieldDescriptor(
                key = "modelTexturesDir",
                label = Res.string.label_modelTexturesDir,
                fileMode = FileMode.DIRECTORIES_ONLY,
            ),

            /** Optional coma separated list of texture type to process [ALBD,COMP,DECAL,NORM], default is all. */
            EnumFieldDescriptor(
                fieldClass = TextureFormat::class,
                key = "textureFormat",
                label = Res.string.label_textureFormat,
                options = { TextureFormat.entries.map { e -> Triple(e.name, null, null) } }
            ),

            /** Determines with which texture flavor the project is working [KTX2, DDS] */
            ListFieldDescriptor(
                fieldClass = TextureType::class,
                key = "textureTypes",
                label = Res.string.label_textureTypes,
                options = { TextureType.entries.map { e -> Triple(e.name, null, null) } }
            ),
        )
    }

    override fun setupFields(
        fieldDescriptors: Map<String, AbstractFieldDescriptor<*, *,*>>
    ): List<Field<*,*,*>> {
        return listOf(
            object : Field<EnumFieldDescriptor<String>, String, String>(
                descriptor = fieldDescriptors["airplaneName"] as EnumFieldDescriptor<String>,
            ) {
                override fun valid(): Boolean = value?.isNotBlank() == true
            },
            object : Field<StringFieldDescriptor, String, String>(
                descriptor = fieldDescriptors["liveryName"] as StringFieldDescriptor,
            ) {
                override fun valid(): Boolean = value?.isNotBlank() == true
            },

            /** Absolute path to the directory containing the layout.json file for your target project. */
            object : Field<FileFieldDescriptor, File, File>(
                descriptor = fieldDescriptors["packageDir"] as FileFieldDescriptor,
            ) {
                override fun valid(): Boolean = value?.exists() == true && value?.isDirectory == true
            },

            /** Absolute path which contains the textures in ktx2 format. */
            object : Field<FileFieldDescriptor, File, File>(
                descriptor = fieldDescriptors["packageTextureDir"] as FileFieldDescriptor,
            ) {
                override fun valid(): Boolean = value?.exists() == true && value?.isDirectory == true
            },

            /** Absolute path which contains the textures in png format (i.e. the directory used by the blender model).  */
            object : Field<FileFieldDescriptor, File, File>(
                descriptor = fieldDescriptors["modelTexturesDir"] as FileFieldDescriptor,
            ) {
                override fun valid(): Boolean = value?.exists() == true && value?.isDirectory == true
            },

            /** Optional coma separated list of texture type to process [ALBD,COMP,DECAL,NORM], default is all. */
            object : Field<EnumFieldDescriptor<TextureFormat>, TextureFormat, TextureFormat>(
                descriptor = fieldDescriptors["textureFormat"] as EnumFieldDescriptor<TextureFormat>,
            ) {
                override fun valid(): Boolean = this.value != null
            },

            /** Determines with which texture flavor the project is working [KTX2, DDS] */
            object : Field<ListFieldDescriptor<TextureType>, MutableList<TextureType>, TextureType>(
                descriptor = fieldDescriptors["textureTypes"] as ListFieldDescriptor<TextureType>,
            ) {
                override fun valid(): Boolean = this.value?.isNotEmpty() == true
            },
        )
    }

    init {
        if (get<TextureFormat>("textureFormat") == null) {
            val packageTextureDir = get<File>("packageTextureDir")
            val dir = packageTextureDir
            val textureFormat = if (dir?.listFiles { file -> file.name.endsWith(".dds", ignoreCase = true) }?.isNotEmpty() == true) {
                TextureFormat.DDS
            } else if (dir?.listFiles { file -> file.name.endsWith(".ktx2", ignoreCase = true) }?.isNotEmpty() == true) {
                TextureFormat.KTX2
            } else {
                null
            }
            set("textureFormat", textureFormat)
        }
    }

    val thumbnailFile: File?
        get() {
            val packageTextureDir = get<File>("packageTextureDir")
            var thumbnailFile: File? = File(packageTextureDir, "thumbnail.png")
            if (thumbnailFile?.exists() == false) thumbnailFile = File(packageTextureDir, "thumbnail.jpg")
            if (thumbnailFile?.exists() == false) thumbnailFile = packageTextureDir?.let { d -> Paths.get(d.canonicalPath, "..", "thumbnail", "thumbnail.png").toFile() }
            if (thumbnailFile?.exists() == false) thumbnailFile = packageTextureDir?.let { d -> Paths.get(d.canonicalPath, "..", "thumbnail", "thumbnail.jpg").toFile() }

        return if (thumbnailFile?.exists() == true) thumbnailFile else null
    }

    override fun createInstance(newFields: LinkedHashMap<String, Field<*, *,*>>): ProjectConfiguration {
        return ProjectConfiguration(fieldDescriptors, newFields, settings)
    }
}
