package de.visualdigits.msfs2024tools.data.database

import de.visualdigits.msfs2024tools.SettingsDatabaseQueries
import de.visualdigits.msfs2024tools.SettingsEntity

fun SettingsDatabaseQueries.upsertSettings(settingsEntity: SettingsEntity) {
    val entity = getSettingsById(settingsEntity.id).executeAsOneOrNull()
    if (entity != null) {
        updateSettings(settingsEntity)
    } else {
        insertSettings(settingsEntity)
    }
}

fun SettingsDatabaseQueries.insertSettings(settingsEntity: SettingsEntity) {
    insertSettings(
        version = settingsEntity.version,
        language = settingsEntity.language,
        simType = settingsEntity.simType,
        sdkRoot = settingsEntity.sdkRoot,
        layoutGeneratorToolPath = settingsEntity.layoutGeneratorToolPath,
        nvidiaTextureToolPath = settingsEntity.nvidiaTextureToolPath,
        mainLibraryRootFolder = settingsEntity.mainLibraryRootFolder,
        projectRootFolder = settingsEntity.projectRootFolder,
        airplanes = settingsEntity.airplanes,
        projects = settingsEntity.projects
    )
}

fun SettingsDatabaseQueries.updateSettings(settingsEntity: SettingsEntity) {
    updateSettings(
        version = settingsEntity.version,
        language = settingsEntity.language,
        simType = settingsEntity.simType,
        sdkRoot = settingsEntity.sdkRoot,
        layoutGeneratorToolPath = settingsEntity.layoutGeneratorToolPath,
        nvidiaTextureToolPath = settingsEntity.nvidiaTextureToolPath,
        mainLibraryRootFolder = settingsEntity.mainLibraryRootFolder,
        projectRootFolder = settingsEntity.projectRootFolder,
        airplanes = settingsEntity.airplanes,
        projects = settingsEntity.projects,
        id = settingsEntity.id
    )
}
