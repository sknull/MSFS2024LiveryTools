package de.visualdigits.msfs2024tools.data.mapper

import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
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

    projectConfiguration.set("airplaneName", airplaneName)
    projectConfiguration.set("liveryName", liveryName)
    projectConfiguration.set("packageDir", packageDir?.let { File(it) })
    projectConfiguration.set("packageTextureDir", packageTextureDir?.let { File(it) })
    projectConfiguration.set("modelTexturesDir", modelTexturesDir?.let { File(it) })
    projectConfiguration.set("textureFormat", textureFormat)
    projectConfiguration.set("textureTypes", textureTypes)

    return projectConfiguration
}

fun ProjectConfiguration.toProjectConfigurationDto(): ProjectConfigurationDto {
    return ProjectConfigurationDto(
        airplaneName = get<String>("airplaneName"),
        liveryName = get<String>("liveryName"),
        packageDir = get<File>("packageDir")?.canonicalPath,
        packageTextureDir = get<File>("packageTextureDir")?.canonicalPath,
        modelTexturesDir = get<File>("modelTexturesDir")?.canonicalPath,
        textureFormat = get<TextureFormat>("textureFormat"),
        textureTypes = get<List<TextureType>>("textureTypes")?:listOf()
    )
}

fun SettingsDto.toSettings(): Settings {
    val settings = Settings()

    settings.set("language", language)
    settings.set("simType", simType)
    settings.set("sdkRoot", File(sdkRoot))
    settings.set("layoutGeneratorToolPath", layoutGeneratorToolPath?.let { File(it) })
    settings.set("nvidiaTextureToolPath", File(nvidiaTextureToolPath))
    settings.set("mainLibraryRootFolder", mainLibraryRootFolder?.let { p -> File(p) })
    settings.set("projectRootFolder", projectRootFolder?.let { p -> File(p) })
    settings.set("airplanes", airplanes)

    return settings
}

fun Settings.toSettingsDto(): SettingsDto {
    val settingsDto = SettingsDto(
        language = get<Language>("language"),
        simType = get<SimType>("simType"),
        sdkRoot = get<File>("sdkRoot")?.canonicalPath?:SDK_ROOT_DEFAULT,
        layoutGeneratorToolPath = get<File>("layoutGeneratorToolPath")?.canonicalPath,
        nvidiaTextureToolPath = get<File>("nvidiaTextureToolPath")?.canonicalPath?:NVIDIA_TEXTURETOOL_PATH_DEFAULT,
        mainLibraryRootFolder = get<File>("mainLibraryRootFolder")?.canonicalPath,
        projectRootFolder = get<File>("projectRootFolder")?.canonicalPath,
        airplanes = get<MutableList<String>>("airplanes")?:mutableListOf()
    )
    return settingsDto
}
