package de.visualdigits.msfs2024tools.data.dto.msfs2024

import de.visualdigits.common.domain.util.writeValueAsXmlString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class ProjectTest {

    @Test
    fun testReadModel() {
        val file = File(ClassLoader.getSystemResource("msfs2024/png-2-ktx2.xml").toURI())
        val project = Project.readValue(file)

        val expected = file.readText()
        val actual = project.writeValueAsXmlString(indent = false)
        assertEquals(expected, actual)
    }

    @Test
    fun testWriteModel() {
        println(Project.PROJECT_DEFAULT.writeValueAsXmlString())
    }
}
