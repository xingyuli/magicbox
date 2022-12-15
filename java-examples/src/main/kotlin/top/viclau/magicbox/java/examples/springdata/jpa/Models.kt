/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.springdata.jpa

import javax.persistence.*

@MappedSuperclass
open class Scoped {
    enum class Scope(private val scope: String) {
        CLUB_UUID("clubUUID");

        fun get(): String {
            return scope
        }
    }

    var scopeName: String? = null
    var scopeValue: String? = null

    @Transient
    fun setScope(scope: Scope, scopeValue: String) {
        scopeName = scope.get()
        this.scopeValue = scopeValue
    }
}

@Entity
@Table(name = "EMPLOYEE")
class Employee : Scoped() {
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Id
    var id: Long? = null
    var name: String? = null
    var age: Int? = null

    @get:JoinColumn(name = "department_id")
    @get:ManyToOne
    var department: Department? = null
}

@Entity
@Table(name = "DEPARTMENT")
class Department : Scoped() {
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Id
    var id: Long? = null
    var name: String? = null

    @get:OneToMany(mappedBy = "department")
    var employees: Set<Employee>? = null
}
