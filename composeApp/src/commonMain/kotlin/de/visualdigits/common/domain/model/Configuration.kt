package de.visualdigits.common.domain.model

import org.jetbrains.compose.resources.StringResource
import java.io.File
import kotlin.reflect.KClass

interface Configuration<T : Configuration<T>> {

    fun labelResource(id: String): StringResource?

    fun toolTipResource(id: String): StringResource?

    /**
     * Returns all current field values as map.
     * Used to render the edit form.
     */
    fun asMap(): Map<String, String?>

    fun clone(): T

    fun update(other: T): T

    operator fun set(key: String, value: String): T

    fun copy(key: String?, value: String?): T

    /**
     * Returns a pair determining the field type (FieldClass, CollectionClass [if any]).
     */
    fun fieldIsEditable(key: String): Boolean

    /**
     * Returns a pair determining the field type (FieldClass, CollectionClass [if any]).
     */
    fun fieldClass(key: String): Pair<Class<*>, Class<*>?>?

    /**
     * Returns the start directory for filepickers (if any).
     */
    fun startDirectory(key: String): File

    /**
     * Returns the possible values as strings for an enum field or the allowed extensions for a file field or an empty list for other fields.
     */
    fun <T : Any> getFieldValues(key: String, clazz: KClass<T>): List<Pair<String, T>>

    /**
     * Specifies the file mode for a File field (either directory or file).
     */
    fun fileMode(key: String): FileMode?

    /**
     * Determines if the actual value of the field is valid or not.
     * Returns a pair of <[true | false], description-resource-id>
     * When the [key] is not given it checks whether the entire configuration is valid or not.
     */
    fun valid(key: String): Boolean?
}

inline fun <reified T : Any> Configuration<*>.fieldValues(key: String): List<Pair<String, T>> {
    return getFieldValues(key, T::class)
}
