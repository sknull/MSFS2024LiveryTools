package de.visualdigits.msfs2024tools.data.model.configuration.migration

import co.touchlab.kermit.Logger
import de.visualdigits.generated.AppVersion
import de.visualdigits.msfs2024tools.data.model.configuration.SettingsDto
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat

object Migration {

    val log = Logger.withTag("Migration")

    private val migrationsSteps = listOf(
        MigrationStep(
            description = "Convert deprecated field textureFormat",
            shouldMigrate = { settingsDto ->
                val foo = settingsDto.version == null || settingsDto.version?.let { v -> AppVersion(v) < AppVersion("1.0.5") } == true
                foo
            },
            targetVersion = AppVersion("1.0.5"),
            projectMigrationStep = { project ->
                // migrate deprecated field textureFormat
                var migrated = false
                if (project.textureFormatPackage == null) {
                    migrated = true
                    project.textureFormatPackage = project.textureFormat
                }
                if (project.textureFormatModel == null) {
                    migrated = true
                    project.textureFormatModel = TextureFormat.PNG
                }
                if (migrated) {
                    project.textureFormat = null
                }

                migrated
            }
        )
    )

    /**
     * Performa all registered migration steps from above on the given [settingsDto]
     * and returns the migrated settings.
     */
    fun doMigrations(settingsDto: SettingsDto): Pair<SettingsDto, Boolean> {
        var migratedSettingsDto = settingsDto
        var migrated = false

        migrationsSteps.forEach { migrationStep ->
            val (settings, stepMigrated) = migrationStep.migrate(migratedSettingsDto)
            migrated = migrated or stepMigrated
            migratedSettingsDto = settings // copy over to chain migration changes (if any)
        }

        return Pair(migratedSettingsDto, migrated)
    }
}
