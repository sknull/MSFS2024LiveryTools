package de.visualdigits.msfs2024tools.data.mapper

import app.cash.sqldelight.ColumnAdapter
import de.visualdigits.msfs2024tools.data.model.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.model.configuration.SettingsDto
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.SK
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings.Companion.NVIDIA_TEXTURETOOL_PATH_DEFAULT
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings.Companion.SDK_ROOT_DEFAULT
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.domain.model.type.SimType
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import kotlinx.serialization.json.Json
import java.io.File

fun ProjectConfigurationDto.toProjectConfiguration(): ProjectConfiguration {
    val projectConfiguration = ProjectConfiguration(mapOf(
        PK.airplaneName to airplaneName,
        PK.liveryName to liveryName,
        PK.packageDir to packageDir?.let { File(it) },
        PK.packageTextureDir to packageTextureDir?.let { File(it) },
        PK.modelTexturesDir to modelTexturesDir?.let { File(it) },
        PK.textureFormatPackage to textureFormatPackage,
        PK.textureFormatModel to textureFormatModel,
        PK.textureTypes to textureTypes
    ))

    return projectConfiguration
}

fun ProjectConfiguration.toProjectConfigurationDto(): ProjectConfigurationDto {
    return ProjectConfigurationDto(
        airplaneName = get<String>(PK.airplaneName),
        liveryName = get<String>(PK.liveryName),
        packageDir = get<File>(PK.packageDir)?.canonicalPath,
        packageTextureDir = get<File>(PK.packageTextureDir)?.canonicalPath,
        modelTexturesDir = get<File>(PK.modelTexturesDir)?.canonicalPath,
        textureFormatPackage = get<TextureFormat>(PK.textureFormatPackage),
        textureFormatModel = get<TextureFormat>(PK.textureFormatModel),
        textureTypes = get<List<TextureType>>(PK.textureTypes)?:listOf()
    )
}

fun SettingsDto.toSettings(): Settings {
    val settings = Settings(mapOf(
        SK.language to language,
        SK.simType to simType,
        SK.sdkRoot to File(sdkRoot),
        SK.layoutGeneratorToolPath to layoutGeneratorToolPath?.let { File(it) },
        SK.nvidiaTextureToolPath to File(nvidiaTextureToolPath),
        SK.mainLibraryRootFolder to mainLibraryRootFolder?.let { p -> File(p) },
        SK.projectRootFolder to projectRootFolder?.let { p -> File(p) },
        SK.airplanes to airplanes,
    ))

    return settings
}

fun Settings.toSettingsDto(): SettingsDto {
    val settingsDto = SettingsDto(
        language = get<Language>(SK.language),
        simType = get<SimType>(SK.simType),
        sdkRoot = get<File>(SK.sdkRoot)?.canonicalPath?:SDK_ROOT_DEFAULT,
        layoutGeneratorToolPath = get<File>(SK.layoutGeneratorToolPath)?.canonicalPath,
        nvidiaTextureToolPath = get<File>(SK.nvidiaTextureToolPath)?.canonicalPath?:NVIDIA_TEXTURETOOL_PATH_DEFAULT,
        mainLibraryRootFolder = get<File>(SK.mainLibraryRootFolder)?.canonicalPath,
        projectRootFolder = get<File>(SK.projectRootFolder)?.canonicalPath,
        airplanes = get<MutableList<String>>(SK.airplanes)?:mutableListOf()
    )
    return settingsDto
}

val stringListAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> = databaseValue.split(",")
    override fun encode(value: List<String>): String = value.joinToString(",")
}

val projectsAdapter = object : ColumnAdapter<List<ProjectConfigurationDto>, String> {
    override fun decode(databaseValue: String): List<ProjectConfigurationDto> =
        if (databaseValue.isEmpty()) listOf() else Json.decodeFromString(databaseValue)

    override fun encode(value: List<ProjectConfigurationDto>): String = Json.encodeToString(value)
}
