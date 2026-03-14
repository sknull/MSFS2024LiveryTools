package de.visualdigits.msfs2024tools.data.dto.msfs2024.assetpackage


import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
class AssetGroups(
    @XmlSerialName("AssetGroup")
    val assetGroup: List<AssetGroup> = listOf()
)
