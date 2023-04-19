/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.stats.model.operator

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.viclau.magicbox.box.stats.model.Query

// TODO viclau t:performance p:low timing the execution
sealed class Operator<DEST_TYPE : Any, in IN, out OUT>(protected val query: Query<DEST_TYPE>) : (IN) -> OUT {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

}
