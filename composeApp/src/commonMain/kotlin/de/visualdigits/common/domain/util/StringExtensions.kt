package de.visualdigits.common.domain.util

/**
 * Expands the variables in format ${name}
 */
fun String.expandVariables(values: Map<String, String?>): String {
    return replace("\\\$\\{(.*?)\\}".toRegex()) { match ->
        val key = match.groupValues[1]
        val value = values[key]
        if (value?.isNotEmpty() == true) value else ""
    }
}
