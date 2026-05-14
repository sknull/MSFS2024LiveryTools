package de.visualdigits.msfs2024tools.data.model.msfs2024.descriptor

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
class UserFlags(
    @XmlSerialName("Type") @XmlElement(false) val type: String = "_DEFAULT",
    @XmlValue val textureQuality: TextureQuality? = TextureQuality.HIGH
)
