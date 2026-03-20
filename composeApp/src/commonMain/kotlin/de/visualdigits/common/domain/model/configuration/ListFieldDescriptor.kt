package de.visualdigits.common.domain.model.configuration

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
open class ListFieldDescriptor<F : Any>(
    fieldClass: KClass<F>,

    key: String,

    label: StringResource,
    toolTip: StringResource? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    options: () -> List<Triple<String, StringResource?, DrawableResource?>> = { listOf() },
    sorted: Boolean = false
): AbstractFieldDescriptor<ListFieldDescriptor<F>, MutableList<F>, F>(
    fieldClass = MutableList::class as KClass<MutableList<F>>,
    singleItemClass = fieldClass,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    options = options,
    sorted = sorted
)
