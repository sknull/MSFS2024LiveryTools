package de.visualdigits.msfs2024tools.data.model.msfs2024.assetpackage


import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
class AssetGroupFlags(
    @XmlSerialName("FSXCompatibility") @XmlElement(true) val fsXCompatibility: Boolean? = null
)
