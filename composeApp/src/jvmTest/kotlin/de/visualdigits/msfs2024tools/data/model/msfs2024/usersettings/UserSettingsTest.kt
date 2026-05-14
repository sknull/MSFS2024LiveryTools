package de.visualdigits.msfs2024tools.data.model.msfs2024.usersettings

import de.visualdigits.msfs2024tools.domain.util.writeValueAsXmlString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class UserSettingsTest {

    @Test
    fun testReadModel() {
        val file = File(ClassLoader.getSystemResource("msfs2024/png-2-ktx2.xml.user").toURI())
        val userSettings = UserSettings.readValue(file)

        val expected = file.readText()
        val actual = userSettings.writeValueAsXmlString(indent = false)
        assertEquals(expected, actual)
    }

    @Test
    fun testWriteModel() {
        println(UserSettings.USER_SETTINGS_DEFAULT.writeValueAsXmlString())
    }
}
