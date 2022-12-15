/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.springdata.jpa.impl

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import top.viclau.magicbox.java.examples.springdata.jpa.CRMService
import top.viclau.magicbox.java.examples.springdata.jpa.Scoped
import top.viclau.magicbox.java.examples.springdata.jpa.SpringDataJPAUnitTest

class CRMServiceImplTest : SpringDataJPAUnitTest() {

    @Autowired
    private lateinit var crmService: CRMService

    @Test
    fun testAddEmployee() {
        val request = QueryRequestImpl()
        request.scope(Scoped.Scope.CLUB_UUID, "swordess")
            .param("name", "vic").param("age", 26)

        val context = QueryContextImpl(request)
        crmService.addEmployee(context)

        assertNull(context.response.error)
    }

}
