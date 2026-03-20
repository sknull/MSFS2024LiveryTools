package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.Enumerable

enum class SimType : Enumerable<SimType> {

    MICROSOFT,
    STEAM
    ;

    override fun fromString(value: String): SimType? {
        return SimType.valueOf(value)
    }
}
