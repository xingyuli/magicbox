/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.dsl.model

import com.google.gson.GsonBuilder
import top.viclau.magicbox.box.stats.dsl.model.operator.*
import top.viclau.magicbox.box.stats.dsl.model.superset.api.SupersetClient
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.reflect.KClass

class SelectWhere<DEST_TYPE>(val select: Select<DEST_TYPE>, val where: Where)

class Query<DEST_TYPE : Any>(
    val destTypeClass: KClass<DEST_TYPE>,
    val config: QueryConfig,
    val selectWheres: List<SelectWhere<DEST_TYPE>>,
    val group: Group<DEST_TYPE>,
    // TODO viclau how to paging if `order` is not specified?
    private val order: Order?,
    private val page: PageReq,
    private val summary: Summary<DEST_TYPE>?
) {

    fun execute(): PageResult<DEST_TYPE> {
        // TODO viclau t:core p:low - clarity - detect select as duplication

        // TODO viclau t:core p:low - chain the operator ?

        /* ***** assemble ***** */

        val assemble = AssembleOperator(this)
        val queryRequests = assemble(Unit)


        /* ***** query ***** */

        val query = QueryOperator(this)
        val queryResponses = query(queryRequests)


        /* ***** combine ***** */

        // TODO viclau t:core p:low - clarity - check that group by column should be annotated with @MetricDimension

        val combine = CombineOperator(this)
        val combinedMetrics = combine(queryResponses)

        /* ***** bind ***** */

        val bind = BindOperator(this)
        val result = bind(BindOperator.In(queryRequests, combinedMetrics))

        val gson = GsonBuilder().disableHtmlEscaping().create()
        result.forEach {
            println(gson.toJson(it))
        }


        /* ***** sort ***** */

        /* ***** page ***** */

        return PageResult(page.page, page.size)
    }

    // TODO later - introduce concept: ExecutionPlan

}

// TODO viclau refactoring - move to Query.Config
interface QueryConfig {
    // TODO viclau t:engine p:low - encapsulate in SupersetQueryEngine.Config { clientConfig: SupersetClient.Config }
    val supersetConfig: SupersetClient.Config
    val rateFn: (Number?, Number?) -> BigDecimal
}

// TODO viclau refactoring - move to Query.DefaultConfig
class DefaultQueryConfig(override val supersetConfig: SupersetClient.Config, precision: Int) : QueryConfig {

    override val rateFn: (Number?, Number?) -> BigDecimal = DivideAsDecimal(precision)

    class DivideAsDecimal(private val precision: Int) : (Number?, Number?) -> BigDecimal {

        override fun invoke(dividend: Number?, divisor: Number?): BigDecimal {
            if (divisor == null || dividend == null || divisor.isZero()) {
                return BigDecimal.ZERO.setScale(precision, RoundingMode.HALF_UP)
            }

            val convertedDividend = BigDecimal(dividend.toString())
            val convertedDivisor = BigDecimal(divisor.toString())

            return convertedDividend.divide(convertedDivisor, precision, RoundingMode.HALF_UP)
        }

        private fun Number.isZero(): Boolean = when (this) {
            is Short -> this.toInt() == 0
            is Int -> this == 0
            is Long -> this.toInt() == 0
            else -> {
                val convertedNum = BigDecimal(toString())
                BigDecimal.ZERO.setScale(convertedNum.scale(), RoundingMode.HALF_UP).equals(convertedNum)
            }
        }

    }

}

