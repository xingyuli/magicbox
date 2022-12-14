package top.viclau.magicbox.java.examples.springdata.jpa

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

interface DepartmentRepository : JpaRepository<Department, Long>

interface EmployeeRepository : JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    object Specs {
        fun olderThan(age: Int): Specification<Employee> {
            return Specification { root: Root<Employee>, query: CriteriaQuery<*>, cb: CriteriaBuilder ->
                cb.gt(
                    root.get(Employee_.age),
                    age
                )
            }
        }
    }
}
