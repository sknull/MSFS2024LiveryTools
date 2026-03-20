package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.Enumerable
import de.visualdigits.common.domain.model.configuration.KeyFactory

enum class SimType : Enumerable<SimType> {

    MICROSOFT,
    STEAM
    ;

    companion object : KeyFactory<SimType> {

        override fun fromString(value: String?): SimType? {
            return SimType.entries.find { e -> e.name == value }
        }

        override fun stringValue(value: Any?): String? = (value as? SimType)?.name
    }
}
