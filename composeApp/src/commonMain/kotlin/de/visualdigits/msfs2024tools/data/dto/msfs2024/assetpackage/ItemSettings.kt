package de.visualdigits.msfs2024tools.data.dto.msfs2024.assetpackage


import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
class ItemSettings(
    @XmlSerialName("ContentType") @XmlElement(true) val contentType: String? = null,
    @XmlSerialName("Title") @XmlElement(true) val title: String? = null,
    @XmlSerialName("Manufacturer") @XmlElement(true) val manufacturer: String? = null,
    @XmlSerialName("Creator") @XmlElement(true) val creator: String? = null
)
