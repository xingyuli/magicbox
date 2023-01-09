/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.kvsj

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JavaBeanAccessorTest {

    @Test
    fun test() {
        val bean = JavaBean()

        // field 'name' of String, with getName and setName
        bean.name = "name"
        assertEquals("name", bean.name)

        // field 'hasName' of boolean, with isHasName and setHasName
        bean.isHasName = true
        assertTrue(bean.isHasName)

        // field 'hasAge' of boolean, with getHasAge and setHasAge
        bean.hasAge = false
        assertFalse(bean.hasAge)

        // field 'isValid' of boolean, with isIsValid and setIsValid
        bean.isIsValid = true
        assertTrue(bean.isIsValid)

        // field 'active' of boolean, with getActive and setActive
        bean.active = true
        assertTrue(bean.active)

        // field 'used' of boolean, with isUsed and setUsed
        bean.isUsed = true
        assertTrue(bean.isUsed)

        assertEquals(90, bean.age)
        assertTrue(bean.hasPressure)
        assertTrue(bean.isOld)
        assertTrue(bean.isFantasy)
    }

}