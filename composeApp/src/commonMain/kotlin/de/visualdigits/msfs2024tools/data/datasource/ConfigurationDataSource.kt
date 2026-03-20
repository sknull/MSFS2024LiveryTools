package de.visualdigits.msfs2024tools.data.datasource

import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto

interface ConfigurationDataSource {

    suspend fun loadSettings(): SettingsDto

    suspend fun saveSettings(settingsDto: SettingsDto)

    suspend fun saveProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto)

    suspend fun updateProjectConfiguration(projectConfigurationDtos: List<ProjectConfigurationDto>)

    suspend fun deleteProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto)
}
