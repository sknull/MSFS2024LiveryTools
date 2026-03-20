package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.Enumerable
import de.visualdigits.common.domain.model.configuration.KeyFactory

enum class TextureFormat : Enumerable<TextureFormat> {
    KTX2,
    DDS,
    PNG
    ;

    companion object : KeyFactory<TextureFormat> {

        override fun fromString(value: String?): TextureFormat? {
            return TextureFormat.entries.find { e -> e.name == value }
        }

        override fun stringValue(value: Any?): String? = (value as? TextureFormat)?.name
    }
}
