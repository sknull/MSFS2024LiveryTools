package de.visualdigits.common.domain.model.configuration

open class Field<V : Any, S : Any, K : FieldKey<K>>(
    val descriptor: AbstractFieldDescriptor<V, S, K>,
    var value: V? = null,
    var enabled: Boolean = true,
    val valid: (value: Any?) -> Boolean = { _ -> true }
) {

    fun fromString(s: String?): V? {
        return descriptor.keyFactory.fromString(s)
    }

    fun stringValue(): String? {
        return descriptor.keyFactory.stringValue(value)
    }

    @Suppress("UNCHECKED_CAST")
    fun copyUnsafe(value: Any? = null): Field<V, S, K> {
        return Field(descriptor, (value as? V)?:this.value, enabled, valid)
    }

    fun copy(value: V?): Field<V, S, K> {
        return Field(descriptor, value?:this.value, enabled, valid)
    }

    @Suppress("UNCHECKED_CAST")
    fun setUnsafe(value: Any?) {
        this.value = value as? V
    }
}
