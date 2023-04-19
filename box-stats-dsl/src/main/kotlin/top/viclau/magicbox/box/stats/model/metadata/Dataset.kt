/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.model.metadata

import top.viclau.magicbox.box.stats.model.operator.QueryRequest
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Attribute Combinations
 *
 * `id` | `resolver` | allowed ? | is custom factory required ? | determined by
 * ---- | ---------- | --------- | ---------------------------- | -------------
 * √    | X          | √         | X                            | `id`
 * √    | √          | X         | -                            | -
 * X    | X          | √         | √                            | customized factory
 * X    | √          | √         | X                            | default primary-no-arg-constructor-based or customized factory
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Dataset(val name: String, val id: String = "", val resolver: KClass<*> = Unit::class) {
    data class CheckAttributeCombinationResult(val ok: Boolean, val requiresCustomFactory: Boolean)
}

fun Dataset.isResolverUndefined(): Boolean = resolver == Unit::class

private fun Dataset.isResolverValid(): Boolean {
    return resolver.isSubclassOf(DatasetIdResolver::class)
}

fun Dataset.meetAttributeCombinationRequirements(): Dataset.CheckAttributeCombinationResult {
    return if (id.isNotEmpty() && isResolverUndefined()) Dataset.CheckAttributeCombinationResult(
        ok = true,
        requiresCustomFactory = false
    )
    else if (id.isNotEmpty() && !isResolverUndefined()) Dataset.CheckAttributeCombinationResult(
        ok = false,
        requiresCustomFactory = false
    )
    else if (id.isEmpty() && isResolverUndefined()) Dataset.CheckAttributeCombinationResult(
        ok = true,
        requiresCustomFactory = true
    )
    else /* id.isEmpty() && !isResolverUndefined() */ Dataset.CheckAttributeCombinationResult(
        ok = isResolverValid(),
        requiresCustomFactory = false
    )
}

interface DatasetIdResolver {
    fun resolve(name: String, request: QueryRequest?): String?
}


// TODO update doc
/**
 * 标识作为统计维度(即会被用在 [Queries.setGroupby] 中)的字段, 标记该注解的字段会在合并指标结果到 JSONObject 的时候被忽略.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class MetricDimension
