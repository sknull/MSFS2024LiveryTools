package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.Enumerable
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import org.jetbrains.compose.resources.DrawableResource

enum class TextureFormat : Enumerable<TextureFormat> {
    KTX2,
    DDS,
    PNG
    ;

    companion object : KeyFactory<TextureFormat> {

        override val options: List<Triple<TextureFormat, UiText?, DrawableResource?>> = TextureFormat.entries.map { e -> Triple(e, null, null) }

        override fun fromString(value: String?): TextureFormat? {
            return TextureFormat.entries.find { e -> e.name == value }
        }

        override fun fromValue(value: Any?): TextureFormat? {
            return when (value) {
                is String -> TextureFormat.Companion.fromString(value)
                is TextureFormat -> value
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? = (value as? TextureFormat)?.name
    }
}
