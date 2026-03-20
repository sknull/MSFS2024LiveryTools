package de.visualdigits.msfs2024tools.data.datasource

import de.visualdigits.common.domain.util.writeValueAsJsonFile
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.SettingsDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Paths

class FilesystemConfigurationDataSource() : ConfigurationDataSource {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val configurationFile = Paths.get(System.getProperty("user.home"), ".msfs2024liverytools", "configuration.json").toFile()

    private var currentSettings: SettingsDto = SettingsDto()

    override suspend fun loadSettings(): SettingsDto = withContext(dispatcher) {
        currentSettings = if (configurationFile.exists()) {
            SettingsDto.readValue(configurationFile)
        } else {
            SettingsDto()
        }

        currentSettings
    }

    override suspend fun saveSettings(settingsDto: SettingsDto) = withContext(dispatcher) {
        val newSettings = settingsDto.clone()
        newSettings.projects.clear()
        newSettings.projects.addAll(currentSettings.projects)
        currentSettings = newSettings
        newSettings.writeValueAsJsonFile(configurationFile)
    }

    override suspend fun saveProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) = withContext(dispatcher) {
        currentSettings.projects
            .removeIf { p -> p.airplaneName == projectConfigurationDto.airplaneName && p.liveryName == projectConfigurationDto.liveryName }
        currentSettings.projects.add(projectConfigurationDto)
        currentSettings.writeValueAsJsonFile(configurationFile)
    }

    override suspend fun updateProjectConfiguration(projectConfigurationDtos: List<ProjectConfigurationDto>) = withContext(dispatcher) {
        currentSettings.projects.clear()
        currentSettings.projects.addAll(projectConfigurationDtos)
        currentSettings.writeValueAsJsonFile(configurationFile)
    }

    override suspend fun deleteProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) = withContext(dispatcher) {
        currentSettings.projects.remove(projectConfigurationDto)
        currentSettings.writeValueAsJsonFile(configurationFile)
    }
}
