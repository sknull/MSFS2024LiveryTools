package de.visualdigits.msfs2024tools.data.repository

import de.visualdigits.common.domain.model.Result
import de.visualdigits.msfs2024tools.data.datasource.ConfigurationDataSource
import de.visualdigits.msfs2024tools.data.mapper.toGlobalConfiguration
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository

class DummyConfigurationRepository(
    val configurationDataSource: ConfigurationDataSource
): ConfigurationRepository {

    override fun loadConfiguration(): Pair<GlobalConfiguration, List<ProjectConfiguration>> {
        val globalConfigurationDto = configurationDataSource.loadConfiguration()
        val globalConfiguration = globalConfigurationDto.toGlobalConfiguration()

        return Pair(globalConfiguration, globalConfigurationDto.projects.map { p -> p.toProjectConfiguration(globalConfiguration) })
    }

    override suspend fun saveGlobalConfiguration(
        globalConfiguration: GlobalConfiguration
    ): Result<Unit, DataError.Local> {
        println("#### saving globalConfiguration: $globalConfiguration")

        return Result.Success(Unit)
    }

    override suspend fun saveProjectConfiguration(
        projectConfiguration: ProjectConfiguration
    ): Result<Unit, DataError.Local> {
        println("#### saving projectConfiguration: $projectConfiguration")

        return Result.Success(Unit)
    }

    override suspend fun updateProjectConfigurations(projectConfigurations: List<ProjectConfiguration>): Result<Unit, DataError.Local> {
        println("#### updating projectConfigurations: $projectConfigurations")

        return Result.Success(Unit)
    }

    override suspend fun deleteProjectConfiguration(
        projectConfiguration: ProjectConfiguration
    ): Result<Unit, DataError.Local> {
        println("#### deleting projectConfiguration: $projectConfiguration")

        return Result.Success(Unit)
    }
}
