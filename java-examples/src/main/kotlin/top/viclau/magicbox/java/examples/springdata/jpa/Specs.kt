/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.springdata.jpa

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

fun <T : Scoped> hasScope(scope: Scoped.Scope, scopeValue: String): Specification<T> {
    return Specification { r: Root<T>, q: CriteriaQuery<*>?, b: CriteriaBuilder ->
        b.and(
            b.equal(r[Scoped_.scopeName], scope.get()),
            b.equal(r[Scoped_.scopeValue], scopeValue)
        )
    }
}
