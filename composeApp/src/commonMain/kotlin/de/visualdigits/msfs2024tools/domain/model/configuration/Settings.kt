package de.visualdigits.msfs2024tools.domain.model.configuration

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.StringFieldDescriptor
import de.visualdigits.common.domain.model.configuration.keyfactory.StringListKeyFactory
import de.visualdigits.common.domain.model.ui.FileMode
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.label_airplanes
import de.visualdigits.compose.resources.label_language
import de.visualdigits.compose.resources.label_layoutGeneratorToolPath
import de.visualdigits.compose.resources.label_mainLibraryRootFolder
import de.visualdigits.compose.resources.label_nvidiaTextureToolPath
import de.visualdigits.compose.resources.label_projectRootFolder
import de.visualdigits.compose.resources.label_sdkRoot
import de.visualdigits.compose.resources.label_simType
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.domain.model.type.SimType
import org.jetbrains.compose.resources.DrawableResource
import java.io.File

@Suppress("UNCHECKED_CAST")
class Settings(
    values: Map<SK, Any?> = mapOf(),
): AbstractConfiguration<Settings, SK>(values, DESCRIPTORS) {

    companion object {

        const val SDK_ROOT_DEFAULT = "C:/MSFS 2024 SDK"

        const val NVIDIA_TEXTURETOOL_PATH_DEFAULT = "C:/Program Files/NVIDIA Corporation/NVIDIA Texture Tools/nvtt_export.exe"

        val DESCRIPTORS = listOf(
            EnumFieldDescriptor(
                fieldClass = Language::class,
                key = SK.language,
                label = UiText.StringResourceId(Res.string.label_language),
                options = { _, _ -> Language.options },
                keyFactory = Language,
                valid = { _, value -> if(value != null) Severity.Info else Severity.Error },
            ),

            EnumFieldDescriptor(
                fieldClass = SimType::class,
                key = SK.simType,
                label = UiText.StringResourceId(Res.string.label_simType),
                options = { _, _ -> SimType.options },
                keyFactory = SimType,
                valid = { _, value -> if(value != null) Severity.Info else Severity.Error }
            ),

            FileFieldDescriptor(
                key = SK.sdkRoot,
                label = UiText.StringResourceId(Res.string.label_sdkRoot),
                fileMode = FileMode.DIRECTORIES_ONLY,
                valid = { _, value -> if(File(value as File, "Tools").exists() && value?.isDirectory == true) Severity.Info else Severity.Error }
            ),

            FileFieldDescriptor(
                key = SK.layoutGeneratorToolPath,
                label = UiText.StringResourceId(Res.string.label_layoutGeneratorToolPath),
                fileMode = FileMode.FILES_ONLY,
                options = { _, _ -> listOf(Triple("exe", null, null)) },
                valid = { _, value -> if(value == null || ((value as? File)?.exists() == true && value.isFile)) Severity.Info else Severity.Error }
            ),

            FileFieldDescriptor(
                key = SK.nvidiaTextureToolPath,
                label = UiText.StringResourceId(Res.string.label_nvidiaTextureToolPath),
                fileMode = FileMode.FILES_ONLY,
                options = { _, _ -> listOf(Triple("exe", null, null)) },
                valid = { _, value -> if((value as? File)?.exists() == true && value.isFile()) Severity.Info else Severity.Error }
            ),

            FileFieldDescriptor(
                key = SK.mainLibraryRootFolder,
                label = UiText.StringResourceId(Res.string.label_mainLibraryRootFolder),
                fileMode = FileMode.DIRECTORIES_ONLY,
                valid = { _, _ -> Severity.Info }
            ),

            FileFieldDescriptor(
                key = SK.projectRootFolder,
                label = UiText.StringResourceId(Res.string.label_projectRootFolder),
                fileMode = FileMode.DIRECTORIES_ONLY,
                valid = { _, _ -> Severity.Info }
            ),

            ListFieldDescriptor(
                fieldClass = String::class,
                key = SK.airplanes,
                label = UiText.StringResourceId(Res.string.label_airplanes),
                visible = false,
                keyFactory = StringListKeyFactory,
                options = { configuration, _ ->
                    configuration.get<List<String>>(SK.airplanes)
                        ?.sorted()
                        ?.map { airplaneName ->
                            Triple<String, UiText?, DrawableResource?>(airplaneName, null, null)
                        }
                        ?:listOf() },
                valid = { _, _ -> Severity.Info }
            ),

            StringFieldDescriptor(
                key = SK.version,
                visible = false,
                label = UiText.DynamicString(""),
            ),

        )
    }

    override fun createInstance(newValues: Map<SK, Any?>): Settings {
        return Settings(newValues)
    }
}
