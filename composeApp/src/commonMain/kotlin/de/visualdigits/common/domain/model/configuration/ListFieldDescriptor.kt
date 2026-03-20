package de.visualdigits.common.domain.model.configuration

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

/**
 * Represents a field which is rendered as an editable list in the UI.
 */
@Suppress("UNCHECKED_CAST")
open class ListFieldDescriptor<F : Any, K : FieldKey<K>>(
    fieldClass: KClass<F>,

    key: K,

    label: StringResource,
    toolTip: StringResource? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    options: () -> List<Triple<String, StringResource?, DrawableResource?>> = { listOf() },

    keyFactory: KeyFactory<MutableList<F>>
): AbstractFieldDescriptor<MutableList<F>, F, K>(
    fieldClass = MutableList::class as KClass<MutableList<F>>,
    itemClass = fieldClass,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    options = options,
    keyFactory = keyFactory
)

class StringListKeyFactory {

    companion object : KeyFactory<MutableList<String>> {

        override fun fromString(value: String?): MutableList<String>  = value?.split(",")?.map { v -> v.trim() }?.toMutableList()?:mutableListOf()

        override fun stringValue(value: Any?): String? {
            val s = (value as? List<String>)?.joinToString(",")
            return s
        }
    }
}
