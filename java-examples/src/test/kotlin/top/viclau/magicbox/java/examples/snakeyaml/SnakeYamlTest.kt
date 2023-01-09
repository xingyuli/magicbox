/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.snakeyaml

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.TypeDescription
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import top.viclau.magicbox.java.examples.absolutePathOf
import top.viclau.magicbox.java.examples.pathUnderPkg
import java.io.File
import java.util.*

class SnakeYamlTest {

    @Test
    fun testLoadFromString() {
        val document = "hello: 25"
        @Suppress("UNCHECKED_CAST")
        val map = Yaml().load(document) as Map<String, *>
        assertEquals("{hello=25}", map.toString())
        assertEquals(25 as Int?, map["hello"])
    }

    @Test
    fun testLoadManyDocuments() {
        File(absolutePathOf(SnakeYamlTest::class.pathUnderPkg("multi_docs.yaml"))!!).inputStream().use { input ->
            var count = 0
            Yaml().loadAll(input).forEach {
                println(it)
                count++
            }
            assertEquals(3, count)
        }
    }

    @Test
    fun testAnyType() {
        val document = """
none: [~, null]
bool: [true, false, on, off]
int: 42
float: 3.14159
list: [LITE, RES_ACID, SUS_DEXT]
map: {hp: 13, sp: 5}
"""
        @Suppress("UNCHECKED_CAST")
        val map = Yaml().load(document) as Map<String, *>
        for ((k, v) in map) {
            println("$k=$v")
        }
    }

    @Test
    fun testGetBeanAssumeClass() {
        val document = """
--- !!top.viclau.magicbox.java.examples.snakeyaml.Person
firstName: Andrey
age: 99
"""
        // consumes the no-args constructor
        val obj: Any = Yaml().load(document)
        assertNotNull(obj)
        assertTrue(obj is Person, "Unexpected: ${obj.javaClass}")

        val person = obj as Person
        assertEquals("Andrey", person.firstName)
        assertEquals(99, person.age)
    }

    @Test
    fun testGetConstructorBean() {
        val document = "--- !!top.viclau.magicbox.java.examples.snakeyaml.Person [ Andrey, 99 ]"
        val obj: Any = Yaml().load(document)
        assertNotNull(obj)
        assertTrue(obj is Person, "Unexpected: ${obj.javaClass}")

        val person = obj as Person
        assertEquals("Andrey", person.firstName)
        assertEquals(99, person.age)
    }

    @Test
    fun testTypeSafeList() {
        val document = """
plate: 12-XP-F4
wheels:
- {id: 1}
- {id: 2}
- {id: 3}
- {id: 4}
- {id: 5}
"""
        val constructor = Constructor(Car::class.java) // Car.class is root
        val carDescription = TypeDescription(Car::class.java)
        carDescription.putListPropertyType("wheels", Wheel::class.java)
        constructor.addTypeDescription(carDescription)

        val obj: Any = Yaml(constructor).load(document)
        assertNotNull(obj)
        assertTrue(obj is Car, "Unexpected: ${obj.javaClass}")

        val car = obj as Car
        assertEquals("12-XP-F4", car.plate)
        assertEquals(5, car.wheels.size)
        with(car.wheels) {
            assertEquals(1, this[0].id)
            assertEquals(2, this[1].id)
            assertEquals(3, this[2].id)
            assertEquals(4, this[3].id)
            assertEquals(5, this[4].id)
        }
    }

    @Test
    fun testTypeSafeMap() {
        val document = """
plate: 00-FF-Q2
wheels:
  ? {brand: Pirelli, id: 1}
  : 2008-01-16
  ? {brand: Dunkel, id: 2}
  : 2002-12-24
  ? {brand: Pirelli, id: 3}
  : 2008-01-16
  ? {brand: Pirelli, id: 4}
  : 2008-01-16
  ? {brand: Pirelli, id: 5}
  : 2008-01-16
"""
        val constructor = Constructor(MyCar::class.java)
        val carDescription = TypeDescription(MyCar::class.java)
        // Please note that both keys and values of the Map can be of any type
        carDescription.putMapPropertyType("wheels", Wheel::class.java, Any::class.java)
        constructor.addTypeDescription(carDescription)

        val obj: Any = Yaml(constructor).load(document)
        assertNotNull(obj)
        assertTrue(obj is MyCar, "Unexpected: ${obj.javaClass}")

        val myCar = obj as MyCar
        println(myCar)
        assertEquals("00-FF-Q2", myCar.plate)
        assertEquals(5, myCar.wheels.size)
    }

    @Test
    fun testDump() {
        val data = mapOf(
            "name" to "Silenthand Olleander",
            "race" to "Human",
            "traits" to arrayOf("ONE_HAND", "ONE_EYE")
        )
        val output = Yaml().dump(data)
        /*
         * Produces:
         * name: Silenthand Olleander
         * race: Human
         * traits: [ONE_HAND, ONE_EYE]
         */
        assertEquals("""
        |name: Silenthand Olleander
        |race: Human
        |traits: [ONE_HAND, ONE_EYE]
        |""".trimMargin(), output)
    }

    @Test
    fun testDumpCustomJavaClass() {
        val data = Car(
            "NOT-FAMOUS",
            listOf(
                Wheel(1, "brandA"),
                Wheel(2, "brand2")
            )
        )
        val output = Yaml().dump(data)
        assertEquals("""
            |!!top.viclau.magicbox.java.examples.snakeyaml.Car
            |plate: NOT-FAMOUS
            |wheels:
            |- {brand: brandA, id: 1}
            |- {brand: brand2, id: 2}
            |""".trimMargin(), output)
    }

}


// produce both no-args and all-args constructor
data class Person(var firstName: String = "", var age: Int = 0)

data class Wheel(var id: Int = 0, var brand: String = "")
data class Car(var plate: String = "", var wheels: List<Wheel> = emptyList())

data class MyCar(var plate: String = "", var wheels: Map<Wheel, Date> = emptyMap())
