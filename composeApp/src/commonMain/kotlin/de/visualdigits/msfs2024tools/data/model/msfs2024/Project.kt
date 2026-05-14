package de.visualdigits.msfs2024tools.data.model.msfs2024

import de.visualdigits.msfs2024tools.data.model.msfs2024.common.Package
import de.visualdigits.msfs2024tools.data.model.msfs2024.common.Packages
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.io.File

@Serializable
class Project(
    @XmlSerialName("Version") @XmlElement(false) val version: Int? = null,
    @XmlSerialName("Name") @XmlElement(false) val name: String? = null,
    @XmlSerialName("FolderName") @XmlElement(false) val folderName: String? = null,
    @XmlSerialName("MetadataFolderName") @XmlElement(false) val metadataFolderName: String? = null,
    @XmlSerialName("OutputDirectory") @XmlElement(true) val outputDirectory: String? = null,
    @XmlSerialName("TemporaryOutputDirectory") @XmlElement(true) val temporaryOutputDirectory: String? = null,
    @XmlSerialName("Packages") @XmlElement(true) val packages: Packages? = null
) {

    companion object {

        val PROJECT_DEFAULT =
            Project(
                version = 2,
                name = "PNG TO KTX2 CONVERTER",
                folderName = "Packages",
                metadataFolderName = "PackagesMetadata",
                outputDirectory = ".",
                temporaryOutputDirectory = "_PackageInt",
                packages = Packages(
                    listOf(
                        Package(
                            value = "PackageDefinitions\\png-2-ktx2.xml"
                        )
                    )
                )
            )

        fun readValue(file: File): Project {
            return try {
                XML.v1.invoke().decodeFromString(file.readText(), null)
            } catch (e: Exception) {
                throw IllegalStateException("Could not parse file '$file'", e)
            }
        }
    }
}
