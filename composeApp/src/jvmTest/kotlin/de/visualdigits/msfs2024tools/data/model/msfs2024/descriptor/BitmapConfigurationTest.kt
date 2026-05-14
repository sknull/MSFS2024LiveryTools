package de.visualdigits.msfs2024tools.data.model.msfs2024.descriptor

import de.visualdigits.msfs2024tools.domain.util.writeValueAsXmlString
import de.visualdigits.msfs2024tools.data.model.configuration.SettingsDto
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class BitmapConfigurationTest {

    private val settings = SettingsDto.readValue(File(ClassLoader.getSystemResource("msfs2024/msfs2024Tools.json").toURI()))

    @Test
    fun testDefaultAlbd() {
        val bitmapConfig =
            BitmapConfiguration(
                textureType = TextureType.ALBD
            )

        val expected = "<BitmapConfiguration><BitmapSlot>MTL_BITMAP_DECAL0</BitmapSlot><UserFlags Type=\"_DEFAULT\">QUALITYHIGH</UserFlags><ForceNoAlpha>false</ForceNoAlpha></BitmapConfiguration>"
        val actual = bitmapConfig.writeValueAsXmlString(indent = false, writeXmlDeclaration = false)

        assertEquals(expected, actual)
    }

    @Test
    fun testDefaultComp() {
        val bitmapConfig =
            BitmapConfiguration(
                textureType = TextureType.COMP
            )

        val expected = "<BitmapConfiguration><BitmapSlot>MTL_BITMAP_METAL_ROUGH_AO</BitmapSlot><UserFlags Type=\"_DEFAULT\">QUALITYHIGH</UserFlags><ForceNoAlpha>false</ForceNoAlpha></BitmapConfiguration>"
        val actual = bitmapConfig.writeValueAsXmlString(indent = false, writeXmlDeclaration = false)

        assertEquals(expected, actual)
    }

    @Test
    fun testDefaultDecal() {
        val bitmapConfig =
            BitmapConfiguration(
                textureType = TextureType.DECAL
            )

        val expected = "<BitmapConfiguration><BitmapSlot>MTL_BITMAP_DECAL0</BitmapSlot><UserFlags Type=\"_DEFAULT\">QUALITYHIGH</UserFlags><ForceNoAlpha>false</ForceNoAlpha></BitmapConfiguration>"
        val actual = bitmapConfig.writeValueAsXmlString(indent = false, writeXmlDeclaration = false)

        assertEquals(expected, actual)
    }

    @Test
    fun testDefaultNorm() {
        val bitmapConfig =
            BitmapConfiguration(
                textureType = TextureType.NORM
            )

        val expected = "<BitmapConfiguration><BitmapSlot>MTL_BITMAP_NORMAL</BitmapSlot><UserFlags Type=\"_DEFAULT\">QUALITYHIGH</UserFlags><ForceNoAlpha>false</ForceNoAlpha></BitmapConfiguration>"
        val actual = bitmapConfig.writeValueAsXmlString(indent = false, writeXmlDeclaration = false)

        assertEquals(expected, actual)
    }
}
