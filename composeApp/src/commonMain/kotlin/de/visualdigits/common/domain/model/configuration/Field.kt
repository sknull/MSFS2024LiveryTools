package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.Enumerable

open class Field<T : AbstractFieldDescriptor<T, V, S>, V : Any, S : Any>(
    val descriptor: AbstractFieldDescriptor<T, V, S>,
    var value: V? = null,
    var enabled: Boolean = true,
) {

    fun fromString(s: String?): V? {
        return if (s == null) {
            null
        } else {
            when (value) {
                is List<*> -> {
                    var list = s.split(",")
                    if (descriptor.sorted) {
                        list = list.sorted()
                    }
                    list.map { v ->
                        if (descriptor.singleItemClass?.let { fc -> Enumerable::class.java.isAssignableFrom(fc.java) } == true) {
                            ((value as? List<Enumerable<*>>)?.firstOrNull() as? Enumerable<*>)?.fromString(v)
                        } else {
                            v
                        }
                    } as V
                }
                is Enumerable<*> -> (value as Enumerable<*>).fromString(s) as V
                else -> value?.toString() as V
            }
        }
    }

    fun stringValue(): String? {
        return when {
            value is List<*> -> {
                var list = value as List<*>
                if (descriptor.sorted) {
                    list = list.sortedBy { v -> v.toString() }
                }
                list
                    .joinToString(",") { v ->
                    when (v) {
                        is Enumerable<*> -> v.name
                        else -> v.toString()
                    }
                }
            }
            value is Enumerable<*> -> (value as Enumerable<*>).name
            else -> value?.toString()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun copyUnsafe(value: Any? = null): Field<T, V, S> {
        return Field(descriptor, (value as? V)?:this.value, enabled)
    }

    fun copy(value: V?): Field<T, V, S> {
        return Field(descriptor, value?:this.value, enabled)
    }

    @Suppress("UNCHECKED_CAST")
    fun setUnsafe(value: Any?) {
        this.value = value as? V
    }

    open fun valid(): Boolean = true
}
