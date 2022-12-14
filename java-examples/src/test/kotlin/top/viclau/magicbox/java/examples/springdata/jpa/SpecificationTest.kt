package top.viclau.magicbox.java.examples.springdata.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification.where
import top.viclau.magicbox.java.examples.springdata.jpa.EmployeeRepository.Specs.olderThan

class SpecificationTest : SpringDataJPAUnitTest() {

    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @Test
    fun testHasScopeSpec() {
        employeeRepository.save(newEmployee("vic", 26, "swordess"))
        employeeRepository.save(newEmployee("jack", 40, "titanic"))

        val found = employeeRepository.findAll(hasScope(Scoped.Scope.CLUB_UUID, "swordess"))
        assertEquals(1, found.size)
        assertEquals("vic", found[0].name)
    }

    @Test
    fun testOlderThanSpec() {
        employeeRepository.save(newEmployee("vic", 26, "swordess"))
        employeeRepository.save(newEmployee("jack", 40, "titanic"))

        val found = employeeRepository.findAll(olderThan(20), Sort.by("id").ascending())
        assertEquals(2, found.size)
        assertEquals("vic", found[0].name)
        assertEquals("jack", found[1].name)
    }

    @Test
    fun testCombinedSpec() {
        employeeRepository.save(newEmployee("vic", 26, "swordess"))
        employeeRepository.save(newEmployee("jack", 40, "titanic"))

        val found1: List<Employee> = employeeRepository.findAll(
            where(olderThan(20)).and(hasScope(Scoped.Scope.CLUB_UUID, "swordess"))
        )
        assertEquals(1, found1.size)
        assertEquals("vic", found1[0].name)

        val found2: List<Employee> = employeeRepository.findAll(
            where(olderThan(30)).and(hasScope(Scoped.Scope.CLUB_UUID, "swordess"))
        )
        assertEquals(0, found2.size)
    }

    private fun newEmployee(name: String, age: Int, clubUUID: String) = Employee().apply {
        this.name = name
        this.age = age
        setScope(Scoped.Scope.CLUB_UUID, clubUUID)
    }

}