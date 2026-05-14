package de.visualdigits.msfs2024tools.data.model.msfs2024.assetpackage

import de.visualdigits.msfs2024tools.domain.util.writeValueAsXmlString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class AssetPackageTest {

    @Test
    fun testReadModel() {
        val file = File(ClassLoader.getSystemResource("msfs2024/PackageDefinitions_png-2-ktx2.xml").toURI())
        val assetPackage = AssetPackage.readValue(file)

        val expected = file.readText()
        val actual = assetPackage.writeValueAsXmlString(indent = false)
        assertEquals(expected, actual)
    }

    @Test
    fun testWriteModel() {
        println(AssetPackage.ASSET_PACKAGE_DEFAULT.writeValueAsXmlString())
    }
}
