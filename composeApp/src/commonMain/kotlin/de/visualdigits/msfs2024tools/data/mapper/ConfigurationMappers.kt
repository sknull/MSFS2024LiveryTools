package de.visualdigits.msfs2024tools.data.mapper

import de.visualdigits.msfs2024tools.data.dto.configuration.GlobalConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import java.io.File

fun ProjectConfigurationDto.toProjectConfiguration(globalConfiguration: GlobalConfiguration): ProjectConfiguration {
    return ProjectConfiguration(
        globalConfiguration = globalConfiguration,
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

fun GlobalConfigurationDto.toGlobalConfiguration(): GlobalConfiguration {
    val globalConfiguration = GlobalConfiguration(
        language = language,
        simType = simType,
        sdkRoot = File(sdkRoot),
        layoutGeneratorToolPath = layoutGeneratorToolPath?.let { File(it) },
        nvidiaTextureToolPath = File(nvidiaTextureToolPath),
        mainLibraryRootFolder = mainLibraryRootFolder?.let { p -> File(p) },
        projectRootFolder = projectRootFolder?.let { p -> File(p) },
        airplanes = airplanes
    )
    return globalConfiguration
}

fun GlobalConfiguration.toGlobalConfigurationDto(): GlobalConfigurationDto {
    val globalConfigurationDto = GlobalConfigurationDto(
        language = language,
        simType = simType,
        sdkRoot = sdkRoot.canonicalPath,
        layoutGeneratorToolPath = layoutGeneratorToolPath?.canonicalPath,
        nvidiaTextureToolPath = nvidiaTextureToolPath.canonicalPath,
        mainLibraryRootFolder = mainLibraryRootFolder?.canonicalPath,
        projectRootFolder = projectRootFolder?.canonicalPath,
        airplanes = airplanes
    )
    return globalConfigurationDto
}
