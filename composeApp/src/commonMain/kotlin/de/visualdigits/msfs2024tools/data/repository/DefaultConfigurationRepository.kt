package de.visualdigits.msfs2024tools.data.repository

import de.visualdigits.common.domain.model.Result
import de.visualdigits.msfs2024tools.data.datasource.ConfigurationDataSource
import de.visualdigits.msfs2024tools.data.mapper.toSettings
import de.visualdigits.msfs2024tools.data.mapper.toSettingsDto
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfiguration
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfigurationDto
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository

class DefaultConfigurationRepository(
    val configurationDataSource: ConfigurationDataSource
): ConfigurationRepository {

    override fun loadConfiguration(): Result<Pair<Settings, List<ProjectConfiguration>>, DataError.Local> {
        val settingsDto = configurationDataSource.loadSettings()
        val settings = settingsDto.toSettings()

        return Result.Success(Pair(settings, settingsDto.projects.map { p -> p.toProjectConfiguration(settings)}))
    }

    override suspend fun saveSettings(
        settings: Settings
    ): Result<Unit, DataError.Local> {
        val settingsDto = settings.toSettingsDto()
        configurationDataSource.saveSettings(settingsDto)

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
