package de.visualdigits.msfs2024tools.data.datasource

import de.visualdigits.msfs2024tools.SettingsEntity
import de.visualdigits.msfs2024tools.data.model.configuration.ProjectConfigurationDto

interface SettingsDataSource {

    suspend fun loadSettings(): SettingsEntity?

    suspend fun saveSettings(settingsEntity: SettingsEntity)

    suspend fun saveProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto)

    suspend fun updateProjectConfiguration(projectConfigurationDtos: List<ProjectConfigurationDto>)

    suspend fun deleteProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto)
}
