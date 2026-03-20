package de.visualdigits.msfs2024tools.domain.service

import de.visualdigits.common.domain.model.Result
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import kotlinx.coroutines.withContext
import java.io.File

interface ConfigurationRepository {

    suspend fun loadConfiguration(): Result<Pair<Settings, List<ProjectConfiguration>>, DataError.Local>

    suspend fun saveSettings(
        settings: Settings
    ): Result<Unit, DataError.Local>

    suspend fun saveProjectConfiguration(
        projectConfiguration: ProjectConfiguration
    ): Result<Unit, DataError.Local>

    suspend fun updateProjectConfigurations(
        projectConfigurations: List<ProjectConfiguration>
    ): Result<Unit, DataError.Local>

    suspend fun deleteProjectConfiguration(
        projectConfiguration: ProjectConfiguration
    ): Result<Unit, DataError.Local>

    suspend fun determineTextureFormat(
        textureDir: File
    ): Result<TextureFormat, DataError.Local>
}
