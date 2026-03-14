package de.visualdigits.msfs2024tools.data.datasource

import de.visualdigits.msfs2024tools.data.dto.configuration.GlobalConfigurationDto
import de.visualdigits.msfs2024tools.data.dto.configuration.ProjectConfigurationDto

interface ConfigurationDataSource {

    fun loadConfiguration(): GlobalConfigurationDto

    suspend fun saveGlobalConfiguration(globalConfigurationDto: GlobalConfigurationDto)

    suspend fun saveProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto)

    suspend fun updateProjectConfiguration(projectConfigurationDtos: List<ProjectConfigurationDto>)

    suspend fun deleteProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto)
}
