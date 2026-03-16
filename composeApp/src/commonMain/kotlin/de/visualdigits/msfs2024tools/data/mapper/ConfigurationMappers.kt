package de.visualdigits.msfs2024tools.data.mapper

import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import java.io.File

fun ProjectConfigurationDto.toProjectConfiguration(settings: Settings): ProjectConfiguration {
    return ProjectConfiguration(
        settings = settings,
        airplaneName = airplaneName,
        liveryName = liveryName,
        packageDir = packageDir?.let { File(it) },
        packageTextureDir = packageTextureDir?.let { File(it) },
        modelTexturesDir = modelTexturesDir?.let { File(it) },
        textureFormat = textureFormat,
        textureTypes = textureTypes,
    )
}

fun ProjectConfiguration.toProjectConfigurationDto(): ProjectConfigurationDto {
    return ProjectConfigurationDto(
        airplaneName = airplaneName,
        liveryName = liveryName,
        packageDir = packageDir?.canonicalPath,
        packageTextureDir = packageTextureDir?.canonicalPath,
        modelTexturesDir = modelTexturesDir?.canonicalPath,
        textureFormat = textureFormat,
        textureTypes = textureTypes
    )
}

fun SettingsDto.toSettings(): Settings {
    val settings = Settings(
        language = language,
        simType = simType,
        sdkRoot = File(sdkRoot),
        layoutGeneratorToolPath = layoutGeneratorToolPath?.let { File(it) },
        nvidiaTextureToolPath = File(nvidiaTextureToolPath),
        mainLibraryRootFolder = mainLibraryRootFolder?.let { p -> File(p) },
        projectRootFolder = projectRootFolder?.let { p -> File(p) },
        airplanes = airplanes
    )
    return settings
}

fun Settings.toSettingsDto(): SettingsDto {
    val settingsDto = SettingsDto(
        language = language,
        simType = simType,
        sdkRoot = sdkRoot.canonicalPath,
        layoutGeneratorToolPath = layoutGeneratorToolPath?.canonicalPath,
        nvidiaTextureToolPath = nvidiaTextureToolPath.canonicalPath,
        mainLibraryRootFolder = mainLibraryRootFolder?.canonicalPath,
        projectRootFolder = projectRootFolder?.canonicalPath,
        airplanes = airplanes
    )
    return settingsDto
}
