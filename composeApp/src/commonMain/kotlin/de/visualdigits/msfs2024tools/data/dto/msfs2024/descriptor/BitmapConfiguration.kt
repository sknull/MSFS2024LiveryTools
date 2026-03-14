package de.visualdigits.msfs2024tools.data.dto.msfs2024.descriptor

import de.visualdigits.msfs2024tools.data.dto.type.TextureQuality
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
class BitmapConfiguration(
    @XmlSerialName("BitmapSlot") @XmlElement(true) val bitmapSlot: String? = null,
    @XmlSerialName("UserFlags") @XmlElement(true) val userFlags: UserFlags? = null,
    @XmlSerialName("ForceNoAlpha") @XmlElement(true) val forceNoAlpha: Boolean? = null
) {

    constructor(
        textureType: TextureType
    ): this(
        bitmapSlot = textureType.bitmapSlot,
        userFlags = UserFlags(
            textureQuality = TextureQuality.HIGH
        ),
        forceNoAlpha = false
    )
}
