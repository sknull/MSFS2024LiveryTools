package de.visualdigits.msfs2024tools.data.model.msfs2024.descriptor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TextureQuality {

    @SerialName("QUALITYHIGH")
    HIGH,

    @SerialName("")
    LOW
}
