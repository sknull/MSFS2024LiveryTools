package de.visualdigits.msfs2024tools.data.repository

import de.visualdigits.common.domain.model.Result
import de.visualdigits.msfs2024tools.data.mapper.toSettingsDto
import de.visualdigits.msfs2024tools.data.mapper.toProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.service.DdsToPngConverter
import de.visualdigits.msfs2024tools.data.service.Ktx2ToPngConverter
import de.visualdigits.msfs2024tools.data.service.PngToDdsConverter
import de.visualdigits.msfs2024tools.data.service.PngToKtx2Converter
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.errorhandling.DataError
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage
import de.visualdigits.msfs2024tools.domain.model.type.Conversion
import de.visualdigits.msfs2024tools.domain.service.Msfs2024Service

class DefaultMsfs2024Service: Msfs2024Service {

    override suspend fun executeConversion(
        configuration: Settings?,
        project: ProjectConfiguration,
        conversion: Conversion,
        dryRun: Boolean,
        progress: (Float) -> Unit,
        logger: (LogMessage) -> Unit
    ): Result<Unit, DataError.Local> {
        return if(configuration != null) {
            val settingsDto = configuration.toSettingsDto()
            val projectConfigurationDto = project.toProjectConfigurationDto()

            when (conversion) {
                Conversion.PNG_TO_DDS -> {
                    PngToDdsConverter.convert(
                        settings = settingsDto,
                        projectConfiguration = projectConfigurationDto,
                        dryRun = dryRun,
                        progress = progress,
                        logger = logger,
                    )
                }
                Conversion.DDS_TO_PNG -> {
                    DdsToPngConverter.convert(
                        settingsDto = settingsDto,
                        projectConfiguration = projectConfigurationDto,
                        dryRun = dryRun,
                        progress = progress,
                        logger = logger,
                    )
                }
                Conversion.PNG_TO_KTX2 -> {
                    PngToKtx2Converter.convert(
                        settingsDto = settingsDto,
                        projectConfiguration = projectConfigurationDto,
                        dryRun = dryRun,
                        progress = progress,
                        logger = logger,
                    )
                }
                Conversion.KTX2_TO_PNG -> {
                    Ktx2ToPngConverter.convert(
                        settingsDto = settingsDto,
                        projectConfiguration = projectConfigurationDto,
                        dryRun = dryRun,
                        progress = progress,
                        logger = logger,
                    )
                }
            }

            Result.Success(Unit)
        } else {
            Result.Error(DataError.Local.SERIALIZATION)
        }
    }
}
