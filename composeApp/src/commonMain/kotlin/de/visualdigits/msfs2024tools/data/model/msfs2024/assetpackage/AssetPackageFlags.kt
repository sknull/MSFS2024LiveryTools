package de.visualdigits.msfs2024tools.data.model.msfs2024.assetpackage


import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
class AssetPackageFlags(
    @XmlSerialName("VisibleInStore") @XmlElement(true) val visibleInStore: Boolean? = null,
    @XmlSerialName("CanBeReferenced") @XmlElement(true) val canBeReferenced: Boolean? = null
)
