package de.visualdigits.msfs2024tools.data.model.configuration

import de.visualdigits.common.domain.util.WindowsUtils.getRunningTasks
import de.visualdigits.msfs2024tools.data.datasource.FilesystemSettingsDataSource
import de.visualdigits.msfs2024tools.data.model.configuration.SetupProjectTest.Companion.setupProject
import de.visualdigits.msfs2024tools.data.repository.DefaultSettingsRepository
import de.visualdigits.msfs2024tools.data.service.PngToKtx2Converter
import de.visualdigits.msfs2024tools.di.platformModule
import de.visualdigits.msfs2024tools.di.sharedModule
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings.Companion.NVIDIA_TEXTURETOOL_PATH_DEFAULT
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings.Companion.SDK_ROOT_DEFAULT
import de.visualdigits.msfs2024tools.domain.model.type.SimType
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import de.visualdigits.msfs2024tools.domain.service.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.inject
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import java.io.File
import kotlin.getValue

class SetupGlobalTest : KoinTest {

    private val settingsRepository by inject<SettingsRepository>()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        allowOverride(true)
        modules(sharedModule, platformModule)
    }

    @Test
    fun testRepository() {
        runBlocking {
            val config = settingsRepository.getSettings()
            println(config)
        }
    }

    @Test
    fun testGetRunningTasks() {
        runBlocking {
            val table = getRunningTasks()
            println(table)
        }
    }

    @Test
    @Disabled("Only for local testing")
    fun testConvert() {
        val config = setupGlobal(
            configFile = File(ClassLoader.getSystemResource("msfs2024/testConfiguration.json").toURI()),
            simType = SimType.MICROSOFT,
            layoutGeneratorToolPath = "C:\\Anwendungen\\MSFSLayoutGenerator.exe"
        )
        setupProject(
            config = config,
            airplaneName = "Boom XB1",
            liveryName = "Nasa",
            packageDir = "E:\\Games\\MSFS 2024\\MainLibrary\\Aircraft\\Airplanes\\Civilian\\Boom XB1\\Boom XB1 Nasa",
            textureSubPath = "SimObjects\\Airplanes\\boom-xb1\\liveries\\VisualDigits\\boom-xb1_nasa\\texture",
            modelTexturesDir = "E:/Games/MSFS 2024/Blender/Airplanes/General Aviation/Boom XB1/Boom XB1_Nasa/texture",
            textureTypes = listOf(TextureType.ALBD, TextureType.COMP)
        )
        runBlocking {
            PngToKtx2Converter.convert(
                settingsDto = config,
                projectConfiguration = config["Boom XB1", "Nasa"]!!,
                progress = { p -> println("#### PROGRESS: $p") },
                logger = { lm -> println("#### $lm") },
                dryRun = false
            )
        }
    }

    @Test
    @Disabled("Only for local dev machine")
    fun testGenerateAppConfig() {
        val config = setupGlobal(
            configFile = File(ClassLoader.getSystemResource("msfs2024/testConfiguration.json").toURI()),
            simType = SimType.MICROSOFT,
            layoutGeneratorToolPath = "C:\\Anwendungen\\MSFSLayoutGenerator.exe"
        )
        setupProject(
            config = config,
            airplaneName = "DA62X",
            liveryName = "Schiller-Ultramarin",
            packageDir = "E:\\Games\\MSFS 2024\\MainLibrary\\Aircraft\\Airplanes\\Civilian\\Diamond DA62X\\FS24_DA62X_Schiller-Ultramarin",
            textureSubPath = "SimObjects\\Airplanes\\DA62X_Schiller-Ultramarin\\texture.Schiller-Ultramarin",
            modelTexturesDir = "E:\\Games\\MSFS 2024\\Blender\\Airplanes\\General Aviation\\Diamond DA62\\DA62_Schiller Ultramarin\\texture",
            textureTypes = listOf(TextureType.ALBD, TextureType.COMP)
        )
        setupProject(
            config = config,
            airplaneName = "Da42VI",
            liveryName = "Schiller-Ultramarin",
            packageDir = "E:\\Games\\MSFS 2024\\MainLibrary\\Aircraft\\Airplanes\\Civilian\\Diamond DA42\\cows-da42vi_Schiller-Ultramarin",
            textureSubPath = "SimObjects\\Airplanes\\da42vi_Schiller-Ultramarin\\texture.Schiller-Ultramarin",
            modelTexturesDir = "E:\\Games\\MSFS 2024\\Blender\\Airplanes\\General Aviation\\Diamond DA42\\cows-da42vi_Schiller Ultramarin\\texture",
            textureTypes = listOf(TextureType.ALBD, TextureType.COMP)
        )
        setupProject(
            config = config,
            airplaneName = "Boom XB1",
            liveryName = "Nasa",
            packageDir = "E:\\Games\\MSFS 2024\\MainLibrary\\Aircraft\\Airplanes\\Civilian\\Boom XB1\\Boom XB1 Nasa",
            textureSubPath = "SimObjects\\Airplanes\\boom-xb1\\liveries\\VisualDigits\\boom-xb1_nasa\\texture",
            modelTexturesDir = "E:/Games/MSFS 2024/Blender/Airplanes/General Aviation/Boom XB1/Boom XB1_Nasa/texture",
            textureTypes = listOf(TextureType.ALBD, TextureType.COMP)
        )
    }

    @Test
    fun testSetupGlobalWithImageTool() {
        File(ClassLoader.getSystemResource(".").toURI()).canonicalPath
        val configFile = File(ClassLoader.getSystemResource("msfs2024/testConfiguration.json").toURI())

        setupGlobal(
            simType = SimType.STEAM,
            layoutGeneratorToolPath = "layoutGeneratorToolPath",
            sdkRoot = "sdkRoot",
            nvidiaTextureToolPath = "nvidiaTextureToolPath",
            configFile = configFile
        )

        val expected = "{\r\n" +
                "    \"language\": \"EN\",\r\n" +
                "    \"simType\": \"STEAM\",\r\n" +
                "    \"sdkRoot\": \"C:\\\\MSFS 2024 SDK\",\r\n" +
                "    \"layoutGeneratorToolPath\": \"C:\\\\Anwendungen\\\\MSFSLayoutGenerator.exe\",\r\n" +
                "    \"flagHQAlbd\": false,\r\n" +
                "    \"flagNoAlphaAlbd\": true,\r\n" +
                "    \"flagHQDecal\": false,\r\n" +
                "    \"flagNoAlphaDecal\": true,\r\n" +
                "    \"nvidiaTextureToolPath\": \"C:\\\\Program Files\\\\NVIDIA Corporation\\\\NVIDIA Texture Tools\\\\nvtt_export.exe\",\r\n" +
                "    \"imageToMSFSKTX2Path\": \"E:\\\\Games\\\\MSFS 2024\\\\ImageToMSFSKTX2\",\r\n" +
                "    \"projects\": []\r\n" +
                "}"

        assertTrue(configFile.exists())
        assertEquals(expected, configFile.readText())
    }

    @Test
    fun testSetupGlobalWithImageToolWithoutConfig() {
        File(ClassLoader.getSystemResource(".").toURI()).canonicalPath
        val configFile = File(ClassLoader.getSystemResource("msfs2024/testConfiguration.json").toURI())

        setupGlobal(
            nvidiaTextureToolPath = "nvidiaTextureToolPath",
            configFile = configFile
        )

        val expected = "{\r\n" +
                "    \"language\": \"EN\",\r\n" +
                "    \"simType\": \"STEAM\",\r\n" +
                "    \"sdkRoot\": \"C:\\\\MSFS 2024 SDK\",\r\n" +
                "    \"layoutGeneratorToolPath\": \"C:\\\\Anwendungen\\\\MSFSLayoutGenerator.exe\",\r\n" +
                "    \"flagHQAlbd\": false,\r\n" +
                "    \"flagNoAlphaAlbd\": true,\r\n" +
                "    \"flagHQDecal\": false,\r\n" +
                "    \"flagNoAlphaDecal\": true,\r\n" +
                "    \"nvidiaTextureToolPath\": \"C:\\\\Program Files\\\\NVIDIA Corporation\\\\NVIDIA Texture Tools\\\\nvtt_export.exe\",\r\n" +
                "    \"imageToMSFSKTX2Path\": \"E:\\\\Games\\\\MSFS 2024\\\\ImageToMSFSKTX2\",\r\n" +
                "    \"projects\": []\r\n" +
                "}"

        assertTrue(configFile.exists())
        val actual = configFile.readText()
        assertEquals(expected, actual)
    }

    @Test
    fun testSetupGlobalWithoutImageTool() {
        val configFile = File(ClassLoader.getSystemResource("msfs2024/testConfiguration.json").toURI())

        setupGlobal(
            nvidiaTextureToolPath = "nvidiaTextureToolPath",
            layoutGeneratorToolPath = "layoutGeneratorToolPath",
            configFile = configFile
        )

        val expected = "{\r\n" +
                "    \"language\": \"EN\",\r\n" +
                "    \"simType\": \"STEAM\",\r\n" +
                "    \"sdkRoot\": \"C:\\\\MSFS 2024 SDK\",\r\n" +
                "    \"layoutGeneratorToolPath\": \"C:\\\\Anwendungen\\\\MSFSLayoutGenerator.exe\",\r\n" +
                "    \"flagHQAlbd\": false,\r\n" +
                "    \"flagNoAlphaAlbd\": true,\r\n" +
                "    \"flagHQDecal\": false,\r\n" +
                "    \"flagNoAlphaDecal\": true,\r\n" +
                "    \"nvidiaTextureToolPath\": \"C:\\\\Program Files\\\\NVIDIA Corporation\\\\NVIDIA Texture Tools\\\\nvtt_export.exe\",\r\n" +
                "    \"imageToMSFSKTX2Path\": \"E:\\\\Games\\\\MSFS 2024\\\\ImageToMSFSKTX2\",\r\n" +
                "    \"projects\": []\r\n" +
                "}"

        val actual = configFile.readText()
        assertEquals(expected, actual)
    }

    fun setupGlobal(
        simType: SimType = SimType.MICROSOFT,
        layoutGeneratorToolPath: String? = null,
        sdkRoot: String = SDK_ROOT_DEFAULT,
        nvidiaTextureToolPath: String = NVIDIA_TEXTURETOOL_PATH_DEFAULT,
        configFile: File,
    ): SettingsDto {
        val config = SettingsDto.readValue(configFile)
        config.nvidiaTextureToolPath = nvidiaTextureToolPath
        config.simType = simType
        config.sdkRoot = sdkRoot
        config.nvidiaTextureToolPath = nvidiaTextureToolPath
        config.layoutGeneratorToolPath = layoutGeneratorToolPath

        return config
    }
}
