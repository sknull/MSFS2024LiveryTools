package de.visualdigits.common.domain.model.configuration

interface KeyFactory<T> {

    fun fromString(value: String?): T?

    fun stringValue(value: Any?): String?
}
