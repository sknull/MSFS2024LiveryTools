package de.visualdigits.msfs2024tools.domain.service

import de.visualdigits.common.domain.model.Result
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError

interface ConfigurationRepository {

    fun loadConfiguration(): Result<Pair<GlobalConfiguration, List<ProjectConfiguration>>, DataError.Local>

    suspend fun saveGlobalConfiguration(
        globalConfiguration: GlobalConfiguration
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
}
