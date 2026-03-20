package de.visualdigits.common.domain.model.configuration

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

abstract class AbstractFieldDescriptor<T : AbstractFieldDescriptor<T, V, S>, V : Any, S : Any>(
    val fieldClass: KClass<V>,
    val singleItemClass: KClass<S>? = null,

    val key: String,

    val label: StringResource,
    val toolTip: StringResource? = null,

    val visible: Boolean = true,
    val readOnly: Boolean = false,

    var options: () -> List<Triple<String, StringResource?, DrawableResource?>> = { listOf() },
    val sorted: Boolean = false
)
