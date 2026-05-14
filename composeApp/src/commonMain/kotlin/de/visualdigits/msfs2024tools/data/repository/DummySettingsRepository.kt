package de.visualdigits.msfs2024tools.data.repository

import de.visualdigits.common.domain.model.errorhandling.Result
import de.visualdigits.msfs2024tools.data.database.toSettings
import de.visualdigits.msfs2024tools.data.datasource.SettingsDataSource
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.domain.service.SettingsRepository
import java.io.File

class DummySettingsRepository(
    val settingsDataSource: SettingsDataSource
): SettingsRepository {

    override suspend fun getSettings(): Result<Pair<Settings, List<ProjectConfiguration>>, DataError.Local> {
        val settingsEntity = settingsDataSource.loadSettings()
        val data = settingsEntity?.toSettings()?:error("No settings found")

        return Result.Success(Pair(data.first, data.second.map { p -> p.toProjectConfiguration() }))
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

    override suspend fun determineTextureFormat(
        textureDir: File
    ): Result<TextureFormat, DataError.Local> {
        println("#### determine texture format for directory: $textureDir")

        return Result.Success(TextureFormat.DDS)
    }
}
