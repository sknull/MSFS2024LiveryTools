package de.visualdigits.msfs2024tools.data.model.msfs2024.common


import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
class Package(
    @XmlSerialName("Name") @XmlElement(false) val name: String? = null,
    @XmlValue val value: String? = null
) {
    constructor(value: String? = null): this(value = value, name = null)
}
