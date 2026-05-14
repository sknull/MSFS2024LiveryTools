package de.visualdigits.msfs2024tools.data.datasource

import de.visualdigits.msfs2024tools.SettingsDatabaseQueries
import de.visualdigits.msfs2024tools.SettingsEntity
import de.visualdigits.msfs2024tools.data.database.upsertSettings
import de.visualdigits.msfs2024tools.data.model.configuration.ProjectConfigurationDto

class DatabaseSettingsDataSource(
    val dao: SettingsDatabaseQueries
) {

    suspend fun loadSettings(): SettingsEntity? {
        return dao.getSettingsById(0).executeAsOneOrNull()
    }

    suspend fun saveSettings(settingsEntity: SettingsEntity) {
        dao.upsertSettings(settingsEntity)
    }

    suspend fun saveProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) {
        val currentSettings = loadSettings()
        val newProjects = currentSettings
            ?.projects
            ?.toMutableList()
            ?:mutableListOf()
        newProjects.removeIf { p -> p.airplaneName == projectConfigurationDto.airplaneName && p.liveryName == projectConfigurationDto.liveryName }
        newProjects.add(projectConfigurationDto)
        currentSettings?.copy(projects = newProjects)?.also { settings -> saveSettings(settings) }
    }

    suspend fun updateProjectConfiguration(projectConfigurationDtos: List<ProjectConfigurationDto>) {
        val currentSettings = loadSettings()
        val newProjects = currentSettings?.projects?.toMutableList()?:mutableListOf()
        newProjects.clear()
        newProjects.addAll(projectConfigurationDtos)
        currentSettings?.copy(projects = newProjects)?.also { settings -> saveSettings(settings) }
    }

    suspend fun deleteProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) {
        val currentSettings = loadSettings()
        val newProjects = currentSettings?.projects?.toMutableList()?:mutableListOf()
        newProjects.remove(projectConfigurationDto)
        currentSettings?.copy(projects = newProjects)?.also { settings -> saveSettings(settings) }
    }
}
