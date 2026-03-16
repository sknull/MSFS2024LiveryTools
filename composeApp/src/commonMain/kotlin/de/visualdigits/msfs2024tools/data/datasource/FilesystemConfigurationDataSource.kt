package de.visualdigits.msfs2024tools.data.datasource

import de.visualdigits.common.domain.util.writeValueAsJsonFile
import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import java.nio.file.Paths

class FilesystemConfigurationDataSource : ConfigurationDataSource {

    private val configurationFile = Paths.get(System.getProperty("user.home"), ".msfs2024liverytools", "configuration.json").toFile()

    private var currentSettings: SettingsDto = SettingsDto()

    override fun loadSettings(): SettingsDto {
        currentSettings = if (configurationFile.exists()) {
            SettingsDto.readValue(configurationFile)
        } else {
            SettingsDto()
        }

        return currentSettings
    }

    override suspend fun saveSettings(settingsDto: SettingsDto) {
        settingsDto.projects.clear()
        settingsDto.projects.addAll(currentSettings.projects)
        currentSettings = settingsDto
        settingsDto.writeValueAsJsonFile(configurationFile)
    }

    override suspend fun saveProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) {
        currentSettings.projects
            .removeIf { p -> p.airplaneName == projectConfigurationDto.airplaneName && p.liveryName == projectConfigurationDto.liveryName }
        currentSettings.projects.add(projectConfigurationDto)
        currentSettings.writeValueAsJsonFile(configurationFile)
    }

    override suspend fun updateProjectConfiguration(projectConfigurationDtos: List<ProjectConfigurationDto>) {
        currentSettings.projects.clear()
        currentSettings.projects.addAll(projectConfigurationDtos)
        currentSettings.writeValueAsJsonFile(configurationFile)
    }

    override suspend fun deleteProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) {
        currentSettings.projects.remove(projectConfigurationDto)
        currentSettings.writeValueAsJsonFile(configurationFile)
    }
}
