package de.visualdigits.msfs2024tools.data.model.msfs2024.common


import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
class Packages(
    @XmlSerialName("Package")
    val `package`: List<Package> = listOf()
)
