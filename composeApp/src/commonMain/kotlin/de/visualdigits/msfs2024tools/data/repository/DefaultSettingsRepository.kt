package de.visualdigits.msfs2024tools.data.repository

import co.touchlab.kermit.Logger
import de.visualdigits.common.domain.model.errorhandling.Result
import de.visualdigits.generated.AppVersion
import de.visualdigits.msfs2024tools.SettingsEntity
import de.visualdigits.msfs2024tools.data.database.toSettings
import de.visualdigits.msfs2024tools.data.database.toSettingsDto
import de.visualdigits.msfs2024tools.data.database.toSettingsEntity
import de.visualdigits.msfs2024tools.data.datasource.DatabaseSettingsDataSource
import de.visualdigits.msfs2024tools.data.datasource.FilesystemSettingsDataSource
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfiguration
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.model.configuration.migration.Migration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.domain.service.SettingsRepository
import java.io.File

class DefaultSettingsRepository(
    val filesystemSettingsDataSource: FilesystemSettingsDataSource,
    val databaseSettingsDataSource: DatabaseSettingsDataSource
): SettingsRepository {

    val log = Logger.withTag("SettingsRepository")

    override suspend fun getSettings(): Result<Pair<Settings, List<ProjectConfiguration>>, DataError.Local> {
        return try {
            val settingsDto = filesystemSettingsDataSource.loadSettings()?.toSettingsDto()
            val settings = if (settingsDto != null) {
                log.i("Found settings from json file")
                val (migratedSettingsDto, migrated) = Migration.doMigrations(settingsDto)
                val migratedSettingsEntity = migratedSettingsDto.toSettingsEntity()
                if (migrated) {
                    log.i("Migrated settings - saving new json file")
                    filesystemSettingsDataSource.saveSettings(migratedSettingsEntity)
                }
                val settingsEnity = databaseSettingsDataSource.loadSettings()
                if (settingsEnity == null) {
                    try {
                        log.i("No database found - converting old json file to database")
                        databaseSettingsDataSource.saveSettings(migratedSettingsEntity)
                        val backupFile = filesystemSettingsDataSource.backupSettingsFile()
                        if (backupFile != null) {
                            FilesystemSettingsDataSource.settingsFile.delete()
                            log.i("Moved old json file from '${FilesystemSettingsDataSource.settingsFile.canonicalPath}' to '${backupFile?.canonicalPath}'")
                        } else {
                            log.w("Could not move old json file to backup folder - keeping it")
                        }
                    } catch (e: Exception) {
                        log.e("Could not save settings", e)
                    }
                }

                migratedSettingsDto.toSettingsEntity()
            } else {
                log.i("Found no settings json file - trying to load database")
                val settingsEnity = databaseSettingsDataSource.loadSettings()
                if (settingsEnity != null) {
                    log.i("Found database")
                    settingsEnity
                } else {
                    log.i("Found no database either - creating default settings and save to database")
                    val newSettings = settingsEnity
                        ?: SettingsEntity(
                            id = 0,
                            version = AppVersion().version,
                            language = "EN",
                            simType = "MICROSOFT",
                            sdkRoot = Settings.SDK_ROOT_DEFAULT,
                            layoutGeneratorToolPath = "",
                            nvidiaTextureToolPath = Settings.NVIDIA_TEXTURETOOL_PATH_DEFAULT,
                            mainLibraryRootFolder = "",
                            projectRootFolder = "",
                            airplanes = listOf(),
                            projects = listOf()
                        )
                    databaseSettingsDataSource.saveSettings(newSettings)
                    newSettings
                }
            }

            val data = settings.toSettings()
            Result.Success(Pair(data.first, data.second.map { p -> p.toProjectConfiguration() }))
        } catch (e: Exception) {
            Logger.e("Could not load configuration", e)
            Result.Error(DataError.Local.SERIALIZATION)
        }
    }

    override suspend fun saveSettings(
        settings: Settings
    ): Result<Unit, DataError.Local> {
        filesystemSettingsDataSource.saveSettings(settings.toSettingsEntity())

        return Result.Success(Unit)
    }

    override suspend fun saveProjectConfiguration(
        projectConfiguration: ProjectConfiguration
    ): Result<Unit, DataError.Local> {
        val projectConfigurationDto = projectConfiguration.toProjectConfigurationDto()
        filesystemSettingsDataSource.saveProjectConfiguration(projectConfigurationDto)

        return Result.Success(Unit)
    }

    override suspend fun updateProjectConfigurations(projectConfigurations: List<ProjectConfiguration>): Result<Unit, DataError.Local> {
        val projectConfigurationDtos = projectConfigurations.map { p -> p.toProjectConfigurationDto() }
        filesystemSettingsDataSource.updateProjectConfiguration(projectConfigurationDtos)

        return Result.Success(Unit)
    }

    override suspend fun deleteProjectConfiguration(projectConfiguration: ProjectConfiguration): Result<Unit, DataError.Local> {
        val projectConfigurationDto = projectConfiguration.toProjectConfigurationDto()
        filesystemSettingsDataSource.deleteProjectConfiguration(projectConfigurationDto)

        return Result.Success(Unit)
    }

    override suspend fun determineTextureFormat(textureDir: File): Result<TextureFormat, DataError.Local> {
        return try {
            when {
                textureDir.listFiles { file -> file.name.endsWith(".dds", ignoreCase = true) }?.isNotEmpty() == true -> {
                    Result.Success(TextureFormat.DDS)
                }
                textureDir.listFiles { file -> file.name.endsWith(".ktx2", ignoreCase = true) }?.isNotEmpty() == true -> {
                    Result.Success(TextureFormat.KTX2)
                }
                textureDir.listFiles { file -> file.name.endsWith(".png", ignoreCase = true) }?.isNotEmpty() == true -> {
                    Result.Success(TextureFormat.PNG)
                }
                else -> {
                    Result.Error(DataError.Local.FILE_NOT_FOUND)
                }
            }
        } catch(e: Exception) {
            Result.Error(DataError.Local.FILE_NOT_FOUND)
        }
    }
}
