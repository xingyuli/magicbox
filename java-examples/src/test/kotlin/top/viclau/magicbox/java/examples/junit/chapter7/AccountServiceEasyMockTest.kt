/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.junit.chapter7

import org.easymock.EasyMock.*
import org.easymock.EasyMockExtension
import org.easymock.Mock
import org.easymock.TestSubject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * See: https://easymock.org/user-guide.html#mocking
 */
@ExtendWith(EasyMockExtension::class)
class AccountServiceEasyMockTest {

    @TestSubject
    private val service = AccountService()

    // Mocks are injected to any field in any @TestSubject that is of compatible type.
    @Mock
    private lateinit var accountManager: AccountManager

    @Test
    fun testTransferOk() {
        val senderAccount = Account("1", 200)
        val beneficiaryAccount = Account("2", 100)

        // start defining the expectations
        accountManager.updateAccount(senderAccount)
        accountManager.updateAccount(beneficiaryAccount)
        expect(accountManager.findAccountForUser("1")).andReturn(senderAccount)
        expect(accountManager.findAccountForUser("2")).andReturn(beneficiaryAccount)

        // we're done defining the expectations
        replay(accountManager)

        service.transfer("1", "2", 50)

        assertEquals(150, senderAccount.balance)
        assertEquals(150, beneficiaryAccount.balance)

        verify(accountManager)
    }

}