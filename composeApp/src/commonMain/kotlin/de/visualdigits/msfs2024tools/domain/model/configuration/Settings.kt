package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import java.io.File

@Suppress("UNCHECKED_CAST")
class Settings(
    fieldDescriptors: LinkedHashMap<String, AbstractFieldDescriptor<*,*,*>> = LinkedHashMap(),
    fields: LinkedHashMap<String, Field<*,*,*>> = LinkedHashMap()
): AbstractConfiguration<Settings>(fieldDescriptors, fields) {

    companion object {

        const val SDK_ROOT_DEFAULT = "C:/MSFS 2024 SDK"

        const val NVIDIA_TEXTURETOOL_PATH_DEFAULT = "C:/Program Files/NVIDIA Corporation/NVIDIA Texture Tools/nvtt_export.exe"
    }

    override fun setupFieldDescriptors(): List<AbstractFieldDescriptor<*, *,*>> {
        return listOf(
            EnumFieldDescriptor(
                fieldClass = Language::class,
                key = "language",
                label = Res.string.label_language,
                options = { Language.entries.map { e -> Triple(e.name, e.stringResourceId, e.drawableResourceId) } }
            ),

            /** Simtype [MICROSFT, STEAM], default is MICROSOFT. */
            EnumFieldDescriptor(
                fieldClass = SimType::class,
                key = "simType",
                label = Res.string.label_simType,
                options = { SimType.entries.map { e -> Triple(e.name, null, null) } }
            ),

            /** Absolute path to the sdk (needed to convert png images to ktx2) default is 'C:/MSFS 2024 SDK'. */
            FileFieldDescriptor(
                key = "sdkRoot",
                label = Res.string.label_sdkRoot,
                fileMode = FileMode.DIRECTORIES_ONLY,
            ),

            /** Absolute path which contains layoutgenerator tool (needed to update layout.json) default is unset. */
            FileFieldDescriptor(
                key = "layoutGeneratorToolPath",
                label = Res.string.label_layoutGeneratorToolPath,
                fileMode = FileMode.FILES_ONLY,
                options = { listOf(Triple("exe", null, null)) }
            ),

            /** Absolute path to the nvidia texture exporter (needed to convert ktx2 to png), default is 'C:/Program Files/NVIDIA Corporation/NVIDIA Texture Tools/nvtt_export.exe' */
            FileFieldDescriptor(
                key = "nvidiaTextureToolPath",
                label = Res.string.label_nvidiaTextureToolPath,
                fileMode = FileMode.FILES_ONLY,
                options = { listOf(Triple("exe", null, null)) }
            ),

            /** Root directory of the sim packages - used as starting directrory for dialogs. */
            FileFieldDescriptor(
                key = "mainLibraryRootFolder",
                label = Res.string.label_mainLibraryRootFolder,
                fileMode = FileMode.DIRECTORIES_ONLY,
            ),

            /** Root directory of the livery projects - used as starting directrory for dialogs. */
            FileFieldDescriptor(
                key = "projectRootFolder",
                label = Res.string.label_projectRootFolder,
                fileMode = FileMode.DIRECTORIES_ONLY,
            ),

            /** The known airplanes */
            ListFieldDescriptor(
                fieldClass = String::class,
                key = "airplanes",
                label = Res.string.label_airplanes,
                visible = false,
                sorted = true,
                options = {
                    get<List<String>>("airplanes")
                        ?.sorted()
                        ?.map { airplaneName ->
                            Triple<String, StringResource?, DrawableResource?>(airplaneName, null, null)
                        }
                        ?:listOf() }
            ),
        )
    }

    override fun setupFields(
        fieldDescriptors: Map<String, AbstractFieldDescriptor<*,*,*>>
    ): List<Field<*,*,*>> {
        return listOf(
            object : Field<EnumFieldDescriptor<Language>, Language, Language>(
                descriptor = fieldDescriptors["language"] as EnumFieldDescriptor<Language>,
            ) {
                override fun valid(): Boolean = this.value != null
            },

            /** Simtype [MICROSFT, STEAM], default is MICROSOFT. */
            object : Field<EnumFieldDescriptor<SimType>, SimType, SimType>(
                descriptor = fieldDescriptors["simType"] as EnumFieldDescriptor<SimType>,
            ) {
                override fun valid(): Boolean = this.value != null
            },

            /** Absolute path to the sdk (needed to convert png images to ktx2) default is 'C:/MSFS 2024 SDK'. */
            object : Field<FileFieldDescriptor, File, File>(
                descriptor = fieldDescriptors["sdkRoot"] as FileFieldDescriptor,
            ) {
                override fun valid(): Boolean = File(value, "Tools").exists() && value?.isDirectory == true
            },

            /** Absolute path which contains layoutgenerator tool (needed to update layout.json) default is unset. */
            object : Field<FileFieldDescriptor, File, File>(
                descriptor = fieldDescriptors["layoutGeneratorToolPath"] as FileFieldDescriptor,
            ) {
                override fun valid(): Boolean = value == null || (value?.exists() == true && value?.isFile == true)
            },

            /** Absolute path to the nvidia texture exporter (needed to convert ktx2 to png), default is 'C:/Program Files/NVIDIA Corporation/NVIDIA Texture Tools/nvtt_export.exe' */
            object : Field<FileFieldDescriptor, File, File>(
                descriptor = fieldDescriptors["nvidiaTextureToolPath"] as FileFieldDescriptor,
            ) {
                override fun valid(): Boolean = value?.exists() == true && value?.isFile() == true
            },

            /** Root directory of the sim packages - used as starting directrory for dialogs. */
            Field(
                descriptor = fieldDescriptors["mainLibraryRootFolder"] as FileFieldDescriptor,
            ),

            /** Root directory of the livery projects - used as starting directrory for dialogs. */
            Field(
                descriptor = fieldDescriptors["projectRootFolder"] as FileFieldDescriptor,
            ),

            /** The known airplanes */
            Field(
                descriptor = fieldDescriptors["airplanes"] as ListFieldDescriptor<String>,
            ),
        )
    }

    override fun createInstance(fields: LinkedHashMap<String, Field<*, *,*>>): Settings {
        return Settings(fieldDescriptors, fields)
    }
}
