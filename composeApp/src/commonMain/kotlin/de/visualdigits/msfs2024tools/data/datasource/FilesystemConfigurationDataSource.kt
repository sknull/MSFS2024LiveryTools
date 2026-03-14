package de.visualdigits.msfs2024tools.data.datasource

import de.visualdigits.common.domain.util.writeValueAsJsonFile
import de.visualdigits.msfs2024tools.data.dto.configuration.GlobalConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto
import java.nio.file.Paths

class FilesystemConfigurationDataSource : ConfigurationDataSource {

    private val configurationFile = Paths.get(System.getProperty("user.home"), ".msfs2024liverytools", "configuration.json").toFile()

    private var currentGlobalConfiguration: GlobalConfigurationDto = GlobalConfigurationDto()

    override fun loadConfiguration(): GlobalConfigurationDto {
        currentGlobalConfiguration = if (configurationFile.exists()) {
            GlobalConfigurationDto.readValue(configurationFile)
        } else {
            GlobalConfigurationDto()
        }

        return currentGlobalConfiguration
    }

    override suspend fun saveGlobalConfiguration(globalConfigurationDto: GlobalConfigurationDto) {
        globalConfigurationDto.projects.clear()
        globalConfigurationDto.projects.addAll(currentGlobalConfiguration.projects)
        currentGlobalConfiguration = globalConfigurationDto
        globalConfigurationDto.writeValueAsJsonFile(configurationFile)
    }

    override suspend fun saveProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) {
        currentGlobalConfiguration.projects
            .removeIf { p -> p.airplaneName == projectConfigurationDto.airplaneName && p.liveryName == projectConfigurationDto.liveryName }
        currentGlobalConfiguration.projects.add(projectConfigurationDto)
        currentGlobalConfiguration.writeValueAsJsonFile(configurationFile)
    }

    override suspend fun updateProjectConfiguration(projectConfigurationDtos: List<ProjectConfigurationDto>) {
        currentGlobalConfiguration.projects.clear()
        currentGlobalConfiguration.projects.addAll(projectConfigurationDtos)
        currentGlobalConfiguration.writeValueAsJsonFile(configurationFile)
    }

    override suspend fun deleteProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) {
        currentGlobalConfiguration.projects.remove(projectConfigurationDto)
        currentGlobalConfiguration.writeValueAsJsonFile(configurationFile)
    }
}
