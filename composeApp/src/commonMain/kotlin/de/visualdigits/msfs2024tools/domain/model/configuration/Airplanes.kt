package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.Enumerable

class Airplanes: Enumerable<Airplanes> {

    override val name: String = ""

    override fun fromString(s: String): Airplanes {
        return Airplanes()
    }
}
