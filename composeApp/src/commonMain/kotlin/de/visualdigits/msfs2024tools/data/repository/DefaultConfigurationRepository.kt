package de.visualdigits.msfs2024tools.data.repository

import de.visualdigits.common.domain.model.Result
import de.visualdigits.msfs2024tools.data.datasource.ConfigurationDataSource
import de.visualdigits.msfs2024tools.data.mapper.toGlobalConfiguration
import de.visualdigits.msfs2024tools.data.mapper.toGlobalConfigurationDto
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfiguration
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfigurationDto
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository

class DefaultConfigurationRepository(
    val configurationDataSource: ConfigurationDataSource
): ConfigurationRepository {

    override fun loadConfiguration(): Pair<GlobalConfiguration, List<ProjectConfiguration>> {
        val globalConfigurationDto = configurationDataSource.loadConfiguration()
        val globalConfiguration = globalConfigurationDto.toGlobalConfiguration()
        return Pair(globalConfiguration, globalConfigurationDto.projects.map { p -> p.toProjectConfiguration(globalConfiguration)})
    }

    override suspend fun saveGlobalConfiguration(
        globalConfiguration: GlobalConfiguration
    ): Result<Unit, DataError.Local> {
        val globalConfigurationDto = globalConfiguration.toGlobalConfigurationDto()
        configurationDataSource.saveGlobalConfiguration(globalConfigurationDto)

        return Result.Success(Unit)
    }

    override suspend fun saveProjectConfiguration(
        projectConfiguration: ProjectConfiguration
    ): Result<Unit, DataError.Local> {
        val projectConfigurationDto = projectConfiguration.toProjectConfigurationDto()
        configurationDataSource.saveProjectConfiguration(projectConfigurationDto)

        return Result.Success(Unit)
    }

    override suspend fun updateProjectConfigurations(projectConfigurations: List<ProjectConfiguration>): Result<Unit, DataError.Local> {
        val projectConfigurationDtos = projectConfigurations.map { p -> p.toProjectConfigurationDto() }
        configurationDataSource.updateProjectConfiguration(projectConfigurationDtos)

        return Result.Success(Unit)
    }

    override suspend fun deleteProjectConfiguration(projectConfiguration: ProjectConfiguration): Result<Unit, DataError.Local> {
        val projectConfigurationDto = projectConfiguration.toProjectConfigurationDto()
        configurationDataSource.deleteProjectConfiguration(projectConfigurationDto)

        return Result.Success(Unit)
    }
}
