package de.visualdigits.msfs2024tools.data.model.configuration

import de.visualdigits.msfs2024tools.domain.util.EmptyStringAsNullSerializer
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration.Companion.TEXTURETYPES_DEFAULT
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import kotlinx.serialization.Serializable

/**
 * Project specific model.
 */
@Serializable
data class ProjectConfigurationDto(

    @Serializable(with = EmptyStringAsNullSerializer::class)
    val airplaneName: String? = null,

    @Serializable(with = EmptyStringAsNullSerializer::class)
    val liveryName: String? = null,

    /** Absolute path to the directory containing the layout.json file for your target project. */
    val packageDir: String? = null,

    /** Absolute path which contains the textures in ktx2 format. */
    val packageTextureDir: String? = null,

    /** Absolute path which contains the textures in png format (i.e. the directory used by the blender model).  */
    val modelTexturesDir: String? = null,

    /** Determines with which texture flavor the project is working [KTX2, DDS] */
    var textureFormatPackage: TextureFormat? = null,

    /** Determines with which texture flavor the project is working [KTX2, DDS] */
    var textureFormatModel: TextureFormat? = null,

    /** Optional coma separated list of texture types to process [ALBD,COMP,DECAL,NORM], default is all. */
    val textureTypes: List<TextureType> = TEXTURETYPES_DEFAULT,

    //
    // Backward compatibility
    //
    /** since 1.0.5 is now split up to textureFormatPackage and textureFormatModel */
    var textureFormat: TextureFormat? = null,
): Comparable<ProjectConfigurationDto> {

    fun clone(): ProjectConfigurationDto {
        return ProjectConfigurationDto(
            airplaneName = airplaneName,
            liveryName = liveryName,
            packageDir = packageDir,
            packageTextureDir = packageTextureDir,
            modelTexturesDir = modelTexturesDir,
            textureFormatPackage = textureFormatPackage,
            textureFormatModel = textureFormatModel,
            textureTypes = textureTypes.toList(),
            textureFormat = textureFormat
        )
    }

    override fun compareTo(other: ProjectConfigurationDto): Int = compareBy<ProjectConfigurationDto>(
        { it.airplaneName },
        { it.liveryName }
    ).compare(this, other)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProjectConfigurationDto

        if (airplaneName != other.airplaneName) return false
        if (liveryName != other.liveryName) return false
        if (packageDir != other.packageDir) return false
        if (packageTextureDir != other.packageTextureDir) return false
        if (modelTexturesDir != other.modelTexturesDir) return false
        if (textureFormatPackage != other.textureFormatPackage) return false
        if (textureTypes != other.textureTypes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = airplaneName?.hashCode() ?: 0
        result = 31 * result + (liveryName?.hashCode() ?: 0)
        result = 31 * result + (packageDir?.hashCode() ?: 0)
        result = 31 * result + (packageTextureDir?.hashCode() ?: 0)
        result = 31 * result + (modelTexturesDir?.hashCode() ?: 0)
        result = 31 * result + (textureFormatPackage?.hashCode() ?: 0)
        result = 31 * result + textureTypes.hashCode()
        return result
    }
}
