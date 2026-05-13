package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import org.jetbrains.compose.resources.DrawableResource

class TextureTypeListKeyFactory {

    companion object : KeyFactory<MutableList<TextureType>> {

        override val options: List<Triple<MutableList<TextureType>, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): MutableList<TextureType>  = value
            ?.split(",")
            ?.map { v -> v.trim() }
            ?.mapNotNull { v -> TextureType.fromString(v) }
            ?.toMutableList()
            ?:mutableListOf()

        override fun fromValue(value: Any?): MutableList<TextureType>? {
            return when (value) {
                is String -> value.split(",").mapNotNull { v -> TextureType.fromString((v.trim())) }.toMutableList()
                is MutableList<*> -> value as? MutableList<TextureType>
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            val s=  (value as? MutableList<TextureType>)?.joinToString(",") { v -> v.name }
            return s
        }
    }
}
