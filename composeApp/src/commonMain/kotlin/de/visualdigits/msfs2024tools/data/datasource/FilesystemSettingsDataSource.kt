package de.visualdigits.msfs2024tools.data.datasource

import de.visualdigits.generated.AppVersion
import de.visualdigits.msfs2024tools.SettingsEntity
import de.visualdigits.msfs2024tools.data.database.toSettingsDto
import de.visualdigits.msfs2024tools.data.database.toSettingsEntity
import de.visualdigits.msfs2024tools.data.model.configuration.ProjectConfigurationDto
import de.visualdigits.msfs2024tools.data.model.configuration.SettingsDto
import de.visualdigits.msfs2024tools.domain.util.writeValueAsJsonFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FilesystemSettingsDataSource() {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    companion object {

        val settingsFile = Paths.get(System.getProperty("user.home"), ".msfs2024liverytools", "configuration.json").toFile()

        val backupDirectory = File(settingsFile.parentFile, "backup")
    }

    private var currentSettings: SettingsDto? = null

    suspend fun loadSettings(): SettingsEntity? = withContext(dispatcher) {
        currentSettings = if (settingsFile.exists()) {
            SettingsDto.readValue(settingsFile)
        } else {
            null
        }

        currentSettings?.toSettingsEntity()
    }

    suspend fun saveSettings(settingsEntity: SettingsEntity) = withContext(dispatcher) {
        val newSettings = settingsEntity.toSettingsDto().clone()
        newSettings.projects.clear()
        newSettings.projects.addAll(currentSettings?.projects?:listOf())
        newSettings.version = AppVersion().version
        currentSettings = newSettings

        backupSettingsFile()
        newSettings.writeValueAsJsonFile(settingsFile)
    }

    fun backupSettingsFile(): File? {
        return if (settingsFile.exists()) {
            if (!backupDirectory.exists() && !backupDirectory.mkdirs()) {
                error("Could not create backupDirectory: ${backupDirectory.canonicalPath}")
            }
            val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
            val backupFile = File(backupDirectory, "${now}_configuration.json")
            settingsFile.renameTo(backupFile)
            backupFile
        } else {
            null
        }
    }

    suspend fun saveProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) = withContext(dispatcher) {
        currentSettings?.projects
            ?.removeIf { p -> p.airplaneName == projectConfigurationDto.airplaneName && p.liveryName == projectConfigurationDto.liveryName }
        currentSettings?.projects?.add(projectConfigurationDto)
        currentSettings?.writeValueAsJsonFile(settingsFile)
    }

    suspend fun updateProjectConfiguration(projectConfigurationDtos: List<ProjectConfigurationDto>) = withContext(dispatcher) {
        currentSettings?.projects?.clear()
        currentSettings?.projects?.addAll(projectConfigurationDtos)
        currentSettings?.writeValueAsJsonFile(settingsFile)
    }

    suspend fun deleteProjectConfiguration(projectConfigurationDto: ProjectConfigurationDto) = withContext(dispatcher) {
        currentSettings?.projects?.remove(projectConfigurationDto)
        currentSettings?.writeValueAsJsonFile(settingsFile)
    }
}
