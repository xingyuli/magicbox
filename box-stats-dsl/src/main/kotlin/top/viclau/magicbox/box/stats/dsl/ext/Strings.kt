/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.ext

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

enum class StringCase : (String) -> String {

    IDENTITY {
        override fun invoke(str: String): String = str
    },

    /**
     * - someFieldName ---> some_field_name
     * - SomeFieldName ---> Some_field_name
     */
    CAMEL_TO_SNAKE {
        override fun invoke(str: String): String = str.camelToSnakeCase()
    },

    /**
     * some_field_name ---> someFieldName
     */
    SNAKE_TO_CAMEL {
        override fun invoke(str: String): String = str.snakeToCamelCase()
    },

}

@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotEmpty != null)
    }
    return !this.isNullOrEmpty()
}

fun String?.toSeparatedSet(separator: Char = ','): Set<String> =
    if (isNotEmpty()) split(separator).toSet() else emptySet()

fun String.camelToSnakeCase(): String {
    // matches any uppercase letter preceded by any character
    val pattern = "(?<=.)[A-Z]".toRegex()
    return replace(pattern) { "_${it.value.lowercase()}" }
}

fun String.snakeToCamelCase(): String {
    val pattern = "_[a-z]".toRegex()
    return replace(pattern) { it.value.last().uppercase() }
}
