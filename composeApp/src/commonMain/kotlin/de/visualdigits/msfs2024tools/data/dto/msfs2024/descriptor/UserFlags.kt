package de.visualdigits.msfs2024tools.data.dto.msfs2024.descriptor

import de.visualdigits.msfs2024tools.data.dto.type.TextureQuality
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
class UserFlags(
    @XmlSerialName("Type") @XmlElement(false) val type: String = "_DEFAULT",
    @XmlValue val textureQuality: TextureQuality? = TextureQuality.HIGH
)
