package de.visualdigits.msfs2024tools.data.model.configuration.migration

import co.touchlab.kermit.Logger
import de.visualdigits.generated.AppVersion
import de.visualdigits.msfs2024tools.data.model.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.model.configuration.SettingsDto
import de.visualdigits.msfs2024tools.data.model.configuration.migration.Migration.log

class MigrationStep(

    /** */
    val description: String,

    /** Should return true if the migration to the [targetVersion] should be done, false otherwise. */
    private val shouldMigrate: (SettingsDto) -> Boolean,

    /** The traget version of this migration step. */
    private val targetVersion: AppVersion,

    /**
     * Migrates the global settings only.
     * Receives a clone and must work on the given [settings]
     * Should return true if something was actually changed, false otherwise.
     */
    private val settingsMigrationStep: ((SettingsDto) -> Boolean)? = null,

    /**
     * Migrates a single project only.
     * Receives a clone and must work on the given [project]
     * Should return true if something was actually changed, false otherwise.
     */
    private val projectMigrationStep: ((ProjectConfigurationDto) -> Boolean)? = null
) {

    /**
     * Clones the given [settingsDto] and migrates them to the [targetVersion] when [shouldMigrate] returns true.
     * Returns the deep copy if something was changed within the migration steps, otherwise returns the unaltered original.
     */
    fun migrate(settingsDto: SettingsDto): Pair<SettingsDto, Boolean> {
        log.i("Executing migration step: $description")
        return if (shouldMigrate(settingsDto)) {
            val newSettings = settingsDto.clone() // deep copy

            var migrated = migrateProjects(newSettings)
            migrated = migrated or migrateSettings(newSettings)

            if (migrated) {
                Pair(newSettings, true)
            } else {
                Pair(settingsDto, false)
            }
        } else {
            Pair(settingsDto, false)
        }
    }

    private fun migrateProjects(
        settingsDto: SettingsDto,
    ): Boolean {
        var migrated = false

        settingsDto.projects.forEach { project ->
            if (projectMigrationStep != null) {
                if (projectMigrationStep(project)) {
                    migrated = true
                    Logger.i("Changes have been made to project '${project.airplaneName}_${project.liveryName}' for target version '${targetVersion.version}'")
                }
            }
        }

        return migrated
    }

    private fun migrateSettings(
        settings: SettingsDto
    ): Boolean {
        var migrated = false

        if (settingsMigrationStep != null) {
            Logger.i("Performing settings migration from version '${settings.version}' to '${targetVersion.version}'")
            if (settingsMigrationStep(settings)) {
                migrated = true
                Logger.i("Changes have been made to settings for target version '${targetVersion.version}'")
            }
        }

        return migrated
    }
}
