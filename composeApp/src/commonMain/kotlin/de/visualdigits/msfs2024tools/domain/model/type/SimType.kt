package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.Enumerable
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import org.jetbrains.compose.resources.DrawableResource

enum class SimType : Enumerable<SimType> {

    MICROSOFT,
    STEAM
    ;

    companion object : KeyFactory<SimType> {

        override val options: List<Triple<SimType, UiText?, DrawableResource?>> = SimType.entries.map { e -> Triple(e, null, null) }

        override fun fromString(value: String?): SimType? {
            return SimType.entries.find { e -> e.name == value }
        }

        override fun fromValue(value: Any?): SimType? {
            return when (value) {
                is String -> SimType.fromString(value)
                is SimType -> value
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? = (value as? SimType)?.name
    }
}
