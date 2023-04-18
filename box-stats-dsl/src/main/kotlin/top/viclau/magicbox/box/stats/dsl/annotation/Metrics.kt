/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Dataset(val name: String)

// TODO update doc
/**
 * 标识作为统计维度(即会被用在 [Queries.setGroupby] 中)的字段, 标记该注解的字段会在合并指标结果到 JSONObject 的时候被忽略.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class MetricDimension
