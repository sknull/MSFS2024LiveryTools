package de.visualdigits.msfs2024tools.data.mapper

import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto
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
import java.io.File

fun ProjectConfigurationDto.toProjectConfiguration(settings: Settings): ProjectConfiguration {
    val projectConfiguration = ProjectConfiguration(settings = settings)

    projectConfiguration.set(PK.airplaneName, airplaneName)
    projectConfiguration.set(PK.liveryName, liveryName)
    projectConfiguration.set(PK.packageDir, packageDir?.let { File(it) })
    projectConfiguration.set(PK.packageTextureDir, packageTextureDir?.let { File(it) })
    projectConfiguration.set(PK.modelTexturesDir, modelTexturesDir?.let { File(it) })
    projectConfiguration.set(PK.textureFormatPackage, textureFormatPackage)
    projectConfiguration.set(PK.textureFormatModel, textureFormatModel)
    projectConfiguration.set(PK.textureTypes, textureTypes)

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
    val settings = Settings()

    settings.set(SK.language, language)
    settings.set(SK.simType, simType)
    settings.set(SK.sdkRoot, File(sdkRoot))
    settings.set(SK.layoutGeneratorToolPath, layoutGeneratorToolPath?.let { File(it) })
    settings.set(SK.nvidiaTextureToolPath, File(nvidiaTextureToolPath))
    settings.set(SK.mainLibraryRootFolder, mainLibraryRootFolder?.let { p -> File(p) })
    settings.set(SK.projectRootFolder, projectRootFolder?.let { p -> File(p) })
    settings.set(SK.airplanes, airplanes)

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
