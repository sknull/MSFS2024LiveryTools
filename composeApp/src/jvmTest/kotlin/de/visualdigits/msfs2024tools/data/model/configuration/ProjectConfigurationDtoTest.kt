package de.visualdigits.msfs2024tools.data.model.configuration

import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertNotNull

class SetupProjectTest {

    companion object {

        fun setupProject(
            config: SettingsDto,
            airplaneName: String,
            liveryName: String,
            packageDir: String,
            textureSubPath: String?,
            modelTexturesDir: String,
            textureTypes: List<TextureType>,
        ) {
            val packageTextureDir = determineTextureSubDir(packageDir, textureSubPath)

            config.addProject(airplaneName, liveryName, packageDir, packageTextureDir, modelTexturesDir, textureTypes)
        }

        private fun determineTextureSubDir(
            packageDir: String?,
            textureSubPath: String?
        ): File {
            val packageTextureDir =
                textureSubPath?.let { tp -> File(packageDir ?: error("No package dir given")).resolve(tp) }
                    ?: File(packageDir ?: error("No package dir given")).walkTopDown()
                        .firstOrNull { it.isDirectory && it.name == "texture" }
                    ?: error("No texture path found")
            check(packageTextureDir.exists()) { "Package texture directory '$packageTextureDir' does not exist" }
            return packageTextureDir
        }
    }

    @Test
    fun testSetupProject() {
        val configFile = File(ClassLoader.getSystemResource("msfs2024/msfs2024Tools.json").toURI())
        val currentDir = configFile.parentFile.parentFile.canonicalPath
        val config = SettingsDto.readValue(configFile)

        setupProject(
            config = config,
            airplaneName = "airplane",
            liveryName = "livery",
            packageDir = File(ClassLoader.getSystemResource("./packageDir").toURI()).canonicalPath,
            textureSubPath = null,
            modelTexturesDir = File(ClassLoader.getSystemResource("./modelTexturesDir").toURI()).canonicalPath,
            textureTypes = listOf(TextureType.ALBD, TextureType.COMP),
        )

        assertTrue(configFile.exists())
        val projectConfiguration = config["airplane", "livery"]
        assertNotNull(projectConfiguration)
        assertEquals("\\packageDir", projectConfiguration.packageDir?.drop(currentDir.length))
        assertEquals("\\modelTexturesDir", projectConfiguration.modelTexturesDir?.drop(currentDir.length))
        val packageTextureDir = projectConfiguration.packageTextureDir
        assertEquals("\\packageDir\\SimObjects\\Airplanes\\projectName\\liveries\\YourName\\projectName\\texture", packageTextureDir?.drop(currentDir.length))
    }
}
