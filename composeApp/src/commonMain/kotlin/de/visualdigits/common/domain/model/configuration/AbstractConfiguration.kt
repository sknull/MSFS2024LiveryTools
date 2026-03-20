package de.visualdigits.common.domain.model.configuration

abstract class AbstractConfiguration<T : AbstractConfiguration<T>>(
    val fieldDescriptors: LinkedHashMap<String, AbstractFieldDescriptor<*,*,*>> = LinkedHashMap(),
    val fields: LinkedHashMap<String, Field<*,*,*>> = LinkedHashMap()
) {

    init {
        if (fieldDescriptors.isEmpty()) {
            setupFieldDescriptors().forEach { fd ->
                fieldDescriptors[fd.key] = fd
            }
            val setupFields = setupFields(fieldDescriptors)
            setupFields.forEach { f ->
                fields[f.descriptor.key] = f
            }
        }
    }

    override fun toString(): String {
        return fields.toList().joinToString(", ") { e -> "${e.first}=\"${e.second}\"" }
    }

    abstract fun setupFieldDescriptors(): List<AbstractFieldDescriptor<*,*,*>>

    abstract fun setupFields(
        fieldDescriptors: Map<String, AbstractFieldDescriptor<*,*,*>>
    ): List<Field<*,*,*>>

    protected abstract fun createInstance(fields: LinkedHashMap<String, Field<*,*,*>>): T

    inline fun <reified V : Any> get(key: String): V? {
        return fields[key]?.value as? V
    }

    @Suppress("UNCHECKED_CAST")
    fun set(key: String, value: Any?) {
        fields[key]?.setUnsafe(value)
    }

    /**
     * Determines if the actual value of the field is valid or not.
     * Returns a pair of <[true | false], description-resource-id>
     * When the [key] is not given it checks whether the entire configuration is valid or not.
     */
    open fun valid(key: String): Boolean? {
        return fields.values.all { field -> field.valid() }
    }

    fun copy(key: String? = null, value: String? = null): T {
        val newFields = LinkedHashMap<String, Field<*,*,*>>()
        fields.values.forEach { f ->
            if (f.descriptor.key == key) {
                newFields[f.descriptor.key] = f.copyUnsafe(f.fromString(value))
            } else {
                newFields[f.descriptor.key] = f.copyUnsafe()
            }
        }

        return createInstance(newFields)
    }
}
