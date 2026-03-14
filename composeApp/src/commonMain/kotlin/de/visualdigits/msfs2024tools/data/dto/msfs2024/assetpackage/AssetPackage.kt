package de.visualdigits.msfs2024tools.data.dto.msfs2024.assetpackage


import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.io.File

@Serializable
class AssetPackage(
    @XmlSerialName("Version") @XmlElement(false) val version: String? = null,
    @XmlSerialName("ItemSettings") val itemSettings: ItemSettings? = null,
    @XmlSerialName("Flags") val flags: AssetPackageFlags? = AssetPackageFlags(),
    @XmlSerialName("AssetGroups") val assetGroups: AssetGroups? = null
) {

    companion object {

        val ASSET_PACKAGE_DEFAULT =
            AssetPackage(
                version = "0.1.0",
                itemSettings = ItemSettings(
                    contentType = "AIRCRAFT",
                    title = "PNG TO KTX2 CONVERTER",
                    manufacturer = "VisualDigits",
                    creator = "VisualDigits",
                ),
                flags = AssetPackageFlags(
                    visibleInStore = true,
                    canBeReferenced = true
                ),
                assetGroups = AssetGroups(
                    listOf(
                        AssetGroup(
                            name = "PNG TO KTX2 CONVERTER",
                            type = "ModularSimObject",
                            flags = AssetGroupFlags(
                                fsXCompatibility = false
                            ),
                            assetDir = "PackageSources\\SimObjects\\Airplanes\\png-2-ktx2\\",
                            outputDir = "SimObjects\\Airplanes\\png-2-ktx2\\"
                        )
                    )
                )
            )

        fun readValue(file: File): AssetPackage {
            return try {
                XML.v1.invoke()
                    .decodeFromString(file.readText(), null)
            } catch (e: Exception) {
                throw IllegalStateException("Could not parse file '$file'", e)
            }
        }
    }
}


