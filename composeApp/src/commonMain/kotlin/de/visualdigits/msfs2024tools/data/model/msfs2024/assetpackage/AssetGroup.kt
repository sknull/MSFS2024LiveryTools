package de.visualdigits.msfs2024tools.data.model.msfs2024.assetpackage

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
class AssetGroup(
    @XmlSerialName("Name") @XmlElement(false) val name: String? = null,
    @XmlSerialName("Type") @XmlElement(true) val type: String? = null,
    @XmlSerialName("Flags") val flags: AssetGroupFlags? = null,
    @XmlSerialName("AssetDir") @XmlElement(true) val assetDir: String? = null,
    @XmlSerialName("OutputDir") @XmlElement(true) val outputDir: String? = null
)
