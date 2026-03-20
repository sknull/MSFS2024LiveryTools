package de.visualdigits.common.domain.model

interface Enumerable<T : Enumerable<T>> {

    val name: String

    fun fromString(value: String): T?
}
