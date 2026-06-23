package de.visualdigits.msfs2024tools.domain.model.configuration

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ReferenceListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.SpacerFieldDescriptor
import de.visualdigits.common.domain.model.configuration.StringFieldDescriptor
import de.visualdigits.common.domain.model.configuration.keyfactory.StringKeyFactory
import de.visualdigits.common.domain.model.ui.FileMode
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.label_airplaneName
import de.visualdigits.compose.resources.label_liveryName
import de.visualdigits.compose.resources.label_modelTexturesDir
import de.visualdigits.compose.resources.label_packageDir
import de.visualdigits.compose.resources.label_packageTextureDir
import de.visualdigits.compose.resources.label_textureFormat
import de.visualdigits.compose.resources.label_textureTypes
import de.visualdigits.compose.resources.tooltip_liveryName
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import kotlinx.io.files.Path
import org.jetbrains.compose.resources.DrawableResource
import java.io.File
import java.nio.file.Paths

@Suppress("UNCHECKED_CAST")
class ProjectConfiguration(
    values: Map<PK, Any?> = mapOf()
): AbstractConfiguration<ProjectConfiguration, PK>(values, DESCRIPTORS) {

    companion object {

        val TEXTURETYPES_DEFAULT = listOf(TextureType.ALBD, TextureType.COMP)

        val DESCRIPTORS = listOf(
            ReferenceListFieldDescriptor<String, PK, SK>(
                fieldClass = String::class,
                key = PK.airplaneName,
                label = UiText.StringResourceId(Res.string.label_airplaneName),
                keyFactory = StringKeyFactory,
                options = { _, settings ->
                    settings?.get<List<String>>(SK.airplanes)
                        ?.sorted()
                        ?.map { airplaneName ->
                            Triple<String, UiText?, DrawableResource?>(airplaneName, null, null)
                        }
                        ?:listOf() },
                valid = { _, value ->
                    if((value as? String)?.isNotBlank() == true) Severity.Info else Severity.Error
                }
            ),

            StringFieldDescriptor(
                key = PK.liveryName,
                label = UiText.StringResourceId(Res.string.label_liveryName),
                toolTip = UiText.StringResourceId(Res.string.tooltip_liveryName),
                valid = { _, value -> if((value as? String)?.isNotBlank() == true) Severity.Info else Severity.Error }
            ),

            FileFieldDescriptor(
                key = PK.packageDir,
                label = UiText.StringResourceId(Res.string.label_packageDir),
                fileMode = FileMode.DIRECTORIES_ONLY,
                valid = { _, value -> if((value as? File)?.exists() == true && value.isDirectory) Severity.Info else Severity.Error }
            ),

            SpacerFieldDescriptor(key = PK.spacer),
                    
            FileFieldDescriptor(
                key = PK.packageTextureDir,
                label = UiText.StringResourceId(Res.string.label_packageTextureDir),
                fileMode = FileMode.DIRECTORIES_ONLY,
                startDirectory = { configuration ->
                    (configuration as ProjectConfiguration).get<Path>(PK.packageDir)
                        ?: Path(System.getProperty("user.home"))
                },
                valid = { _, value -> if((value as? File)?.exists() == true && value.isDirectory) Severity.Info else Severity.Error }
            ),
            
            EnumFieldDescriptor(
                fieldClass = TextureFormat::class,
                key = PK.textureFormatPackage,
                readOnly = true,
                label = UiText.StringResourceId(Res.string.label_textureFormat),
                options = { _, _ -> TextureFormat.options },
                keyFactory = TextureFormat,
                valid = { _, value -> if(value != null) Severity.Info else Severity.Error }
            ),

            FileFieldDescriptor(
                key = PK.modelTexturesDir,
                label = UiText.StringResourceId(Res.string.label_modelTexturesDir),
                fileMode = FileMode.DIRECTORIES_ONLY,
                valid = { _, value -> if((value as? File)?.exists() == true && value.isDirectory) Severity.Info else Severity.Error }
            ),

            EnumFieldDescriptor(
                fieldClass = TextureFormat::class,
                key = PK.textureFormatModel,
                readOnly = true,
                label = UiText.StringResourceId(Res.string.label_textureFormat),
                options = { _, _ -> TextureFormat.options },
                keyFactory = TextureFormat,
                valid = { _, value -> if(value != null) Severity.Info else Severity.Error }
            ),

            ListFieldDescriptor(
                fieldClass = TextureType::class,
                key = PK.textureTypes,
                label = UiText.StringResourceId(Res.string.label_textureTypes),
                options = { _, _ -> TextureType.options },
                keyFactory = TextureTypeListKeyFactory,
                valid = { _, value -> if((value as? MutableList<TextureType>)?.isNotEmpty() == true) Severity.Info else Severity.Error }
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

    override fun createInstance(newValues: Map<PK, Any?>): ProjectConfiguration {
        return ProjectConfiguration(newValues)
    }
}

