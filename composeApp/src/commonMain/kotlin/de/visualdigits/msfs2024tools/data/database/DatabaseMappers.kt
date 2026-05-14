package de.visualdigits.msfs2024tools.data.database

import de.visualdigits.common.domain.model.configuration.AbstractConfiguration.Companion.valueMap
import de.visualdigits.generated.AppVersion
import de.visualdigits.msfs2024tools.SettingsEntity
import de.visualdigits.msfs2024tools.data.model.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.model.configuration.SettingsDto
import de.visualdigits.msfs2024tools.domain.model.configuration.SK
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.domain.model.type.SimType
import java.io.File

fun Settings.toSettingsEntity(
    projectConfigurationDtos: List<ProjectConfigurationDto> = listOf()
): SettingsEntity {
    return SettingsEntity(
        id = 0,
        version = AppVersion().version,
        language = get<Language>(SK.language)?.name ?: "EN",
        simType = get<SimType>(SK.simType)?.name ?: "MICROSOFT",
        sdkRoot = get<Language>(SK.sdkRoot)?.name ?: Settings.SDK_ROOT_DEFAULT,
        layoutGeneratorToolPath = get<File>(SK.layoutGeneratorToolPath)?.name ?: "",
        nvidiaTextureToolPath = get<File>(SK.nvidiaTextureToolPath)?.name ?: Settings.NVIDIA_TEXTURETOOL_PATH_DEFAULT,
        mainLibraryRootFolder = get<File>(SK.mainLibraryRootFolder)?.name ?: "",
        projectRootFolder = get<File>(SK.projectRootFolder)?.name ?: "",
        airplanes = get<List<String>>(SK.airplanes) ?: listOf(),
        projects = projectConfigurationDtos
    )
}

fun SettingsEntity.toSettings(): Pair<Settings, List<ProjectConfigurationDto>> {
    val settings = Settings(
        valueMap(
            fieldDescriptors = Settings.DESCRIPTORS,
            values = mapOf(
                SK.version to version,
                SK.language to Language.fromValue(language),
                SK.simType to SimType.fromValue(simType),
                SK.sdkRoot to File(sdkRoot),
                SK.layoutGeneratorToolPath to File(layoutGeneratorToolPath),
                SK.nvidiaTextureToolPath to File(nvidiaTextureToolPath),
                SK.mainLibraryRootFolder to File(mainLibraryRootFolder),
                SK.projectRootFolder to File(projectRootFolder),
                SK.airplanes to airplanes,
            )
        )
    )
    return Pair(settings, projects)
}

fun SettingsEntity.toSettingsDto(): SettingsDto {
    return SettingsDto(
        version = version,
        language = Language.fromValue(language),
        simType = SimType.fromValue(simType),
        sdkRoot = sdkRoot,
        layoutGeneratorToolPath = layoutGeneratorToolPath,
        nvidiaTextureToolPath = nvidiaTextureToolPath,
        mainLibraryRootFolder = mainLibraryRootFolder,
        projectRootFolder = projectRootFolder,
        airplanes = airplanes.toMutableList(),
        projects = projects.toMutableList()
    )
}

fun SettingsDto.toSettingsEntity(): SettingsEntity {
    return SettingsEntity(
        version = version?: AppVersion().version,
        language = language?.name?:"EN",
        simType = simType?.name?:"MICROSOFT",
        sdkRoot = sdkRoot,
        layoutGeneratorToolPath = layoutGeneratorToolPath?:"",
        nvidiaTextureToolPath = nvidiaTextureToolPath,
        mainLibraryRootFolder = mainLibraryRootFolder?:"",
        projectRootFolder = projectRootFolder?:"",
        airplanes = airplanes.toMutableList(),
        projects = projects.toMutableList(),
        id = 0
    )
}
