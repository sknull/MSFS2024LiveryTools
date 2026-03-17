package de.visualdigits.msfs2024tools.data.repository

import de.visualdigits.common.domain.model.Result
import de.visualdigits.msfs2024tools.data.datasource.ConfigurationDataSource
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfiguration
import de.visualdigits.msfs2024tools.data.mapper.toSettings
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository

class DummyConfigurationRepository(
    val configurationDataSource: ConfigurationDataSource
): ConfigurationRepository {

    override fun loadConfiguration(): Result<Pair<Settings, List<ProjectConfiguration>>, DataError.Local> {
        val settingsDto = configurationDataSource.loadSettings()
        val settings = settingsDto.toSettings()

        return Result.Success(Pair(settings, settingsDto.projects.map { p -> p.toProjectConfiguration(settings) }))
    }

    override suspend fun saveSettings(
        settings: Settings
    ): Result<Unit, DataError.Local> {
        println("#### saving settings: $settings")

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
