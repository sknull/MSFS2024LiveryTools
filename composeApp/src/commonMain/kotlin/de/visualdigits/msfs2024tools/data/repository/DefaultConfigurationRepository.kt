package de.visualdigits.msfs2024tools.data.repository

import co.touchlab.kermit.Logger
import de.visualdigits.common.domain.model.Result
import de.visualdigits.msfs2024tools.data.datasource.ConfigurationDataSource
import de.visualdigits.msfs2024tools.data.dto.configuration.migration.Migration
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfiguration
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.mapper.toSettings
import de.visualdigits.msfs2024tools.data.mapper.toSettingsDto
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository
import java.io.File

class DefaultConfigurationRepository(
    val configurationDataSource: ConfigurationDataSource
): ConfigurationRepository {

    override suspend fun loadConfiguration(): Result<Triple<Settings, List<ProjectConfiguration>, Boolean>, DataError.Local> {
        return try {
            val settingsDto = configurationDataSource.loadSettings()

            val (migratedSettingsDto, migrated) = Migration.doMigrations(settingsDto)
            if (migrated) {
                configurationDataSource.saveSettings(migratedSettingsDto)
            }

            val settings = migratedSettingsDto.toSettings()

            Result.Success(Triple(settings, settingsDto.projects.map { p -> p.toProjectConfiguration(settings)}, migrated))
        } catch (e: Exception) {
            Logger.e("Could not load configuration", e)
            Result.Error(DataError.Local.SERIALIZATION)
        }

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

    override suspend fun determineTextureFormat(textureDir: File): Result<TextureFormat, DataError.Local> {
        return try {
            if (textureDir.listFiles { file -> file.name.endsWith(".dds", ignoreCase = true) }?.isNotEmpty() == true) {
                Result.Success(TextureFormat.DDS)
            } else if (textureDir.listFiles { file -> file.name.endsWith(".ktx2", ignoreCase = true) }?.isNotEmpty() == true) {
                Result.Success(TextureFormat.KTX2)
            } else if (textureDir.listFiles { file -> file.name.endsWith(".png", ignoreCase = true) }?.isNotEmpty() == true) {
                Result.Success(TextureFormat.PNG)
            } else {
                Result.Error(DataError.Local.FILE_NOT_FOUND)
            }
        } catch(e: Exception) {
            Result.Error(DataError.Local.FILE_NOT_FOUND)
        }
    }
}
