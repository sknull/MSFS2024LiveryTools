package de.visualdigits.common.util

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("resources")
data class XmlResources(
    @XmlSerialName("string") val strings: MutableList<ResourceString> = mutableListOf()
)
