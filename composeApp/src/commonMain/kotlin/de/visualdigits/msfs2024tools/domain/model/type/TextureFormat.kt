package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.Enumerable

enum class TextureFormat : Enumerable<TextureFormat> {
    KTX2,
    DDS
    ;

    override fun fromString(value: String): TextureFormat? {
        return TextureFormat.valueOf(value)
    }
}
