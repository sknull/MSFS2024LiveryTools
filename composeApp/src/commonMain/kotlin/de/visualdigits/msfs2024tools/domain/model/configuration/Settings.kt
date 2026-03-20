package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.StringListKeyFactory
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
    fields: LinkedHashMap<SK, Field<*,*,SK>> = LinkedHashMap()
): AbstractConfiguration<Settings, SK>(fields) {

    companion object {

        const val SDK_ROOT_DEFAULT = "C:/MSFS 2024 SDK"

        const val NVIDIA_TEXTURETOOL_PATH_DEFAULT = "C:/Program Files/NVIDIA Corporation/NVIDIA Texture Tools/nvtt_export.exe"
    }

    override fun setupFields(): List<Field<*,*,SK>> {
        return listOf(
            /** The UI language. */
            Field(
                descriptor = EnumFieldDescriptor(
                    fieldClass = Language::class,
                    key = SK.language,
                    label = Res.string.label_language,
                    options = { Language.entries.map { e -> Triple(e.name, e.stringResourceId, e.drawableResourceId) } },
                    keyFactory = Language
                ),
                valid = { value -> value != null }
            ),

            /** Simtype [MICROSFT, STEAM], default is MICROSOFT. */
            Field(
                descriptor = EnumFieldDescriptor(
                    fieldClass = SimType::class,
                    key = SK.simType,
                    label = Res.string.label_simType,
                    options = { SimType.entries.map { e -> Triple(e.name, null, null) } },
                    keyFactory = SimType
                ),
                valid = { value -> value != null }
            ),

            /** Absolute path to the sdk (needed to convert png images to ktx2) default is 'C:/MSFS 2024 SDK'. */
            Field(
                descriptor = FileFieldDescriptor(
                    key = SK.sdkRoot,
                    label = Res.string.label_sdkRoot,
                    fileMode = FileMode.DIRECTORIES_ONLY,
                ),
                valid = { value -> File(value as File, "Tools").exists() && value?.isDirectory == true }
            ),

            /** Absolute path which contains layoutgenerator tool (needed to update layout.json) default is unset. */
            Field(
                descriptor = FileFieldDescriptor(
                    key = SK.layoutGeneratorToolPath,
                    label = Res.string.label_layoutGeneratorToolPath,
                    fileMode = FileMode.FILES_ONLY,
                    options = { listOf(Triple("exe", null, null)) }
                ),
                valid = { value -> value == null || ((value as? File)?.exists() == true && value.isFile) }
            ),

            /** Absolute path to the nvidia texture exporter (needed to convert ktx2 to png), default is 'C:/Program Files/NVIDIA Corporation/NVIDIA Texture Tools/nvtt_export.exe' */
            Field(
                descriptor = FileFieldDescriptor(
                    key = SK.nvidiaTextureToolPath,
                    label = Res.string.label_nvidiaTextureToolPath,
                    fileMode = FileMode.FILES_ONLY,
                    options = { listOf(Triple("exe", null, null)) }
                ),
                valid = { value -> (value as? File)?.exists() == true && value.isFile() }
            ),

            /** Root directory of the sim packages - used as starting directrory for dialogs. */
            Field(
                descriptor = FileFieldDescriptor(
                    key = SK.mainLibraryRootFolder,
                    label = Res.string.label_mainLibraryRootFolder,
                    fileMode = FileMode.DIRECTORIES_ONLY,
                ),
                valid = { _ -> true }
            ),

            /** Root directory of the livery projects - used as starting directrory for dialogs. */
            Field(
                descriptor = FileFieldDescriptor(
                    key = SK.projectRootFolder,
                    label = Res.string.label_projectRootFolder,
                    fileMode = FileMode.DIRECTORIES_ONLY,
                ),
                valid = { _ -> true }
            ),

            /** The known airplanes */
            Field(
                descriptor = ListFieldDescriptor(
                    fieldClass = String::class,
                    key = SK.airplanes,
                    label = Res.string.label_airplanes,
                    visible = false,
                    keyFactory = StringListKeyFactory,
                    options = {
                        get<List<String>>(SK.airplanes)
                            ?.sorted()
                            ?.map { airplaneName ->
                                Triple<String, StringResource?, DrawableResource?>(airplaneName, null, null)
                            }
                            ?:listOf() }
                ),
                valid = { _ -> true }
            ),
        )
    }

    override fun createInstance(newFields: LinkedHashMap<SK, Field<*,*,SK>>): Settings {
        return Settings(newFields)
    }
}
