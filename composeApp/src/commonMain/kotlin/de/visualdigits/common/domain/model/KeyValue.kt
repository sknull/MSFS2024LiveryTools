package de.visualdigits.common.domain.model

class KeyValue(
    val key: String,
    val value: String? = null,
    val previousValue: String? = null,
    val newValue: String? = null,
) {

    override fun toString(): String = "keyValueAction: $key=$value"
}
