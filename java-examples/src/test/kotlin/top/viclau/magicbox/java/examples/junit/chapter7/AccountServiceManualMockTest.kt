/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.junit.chapter7

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AccountServiceManualMockTest {

    @Test
    fun testTransferOk() {
        // 1. preparation
        val senderAccount = Account("1", 200)
        val beneficiaryAccount = Account("2", 100)
        val manager = MockAccountManager()
        manager.addAccount("1", senderAccount)
        manager.addAccount("2", beneficiaryAccount)
        val service = AccountService()
        service.setAccountManager(manager)

        // 2. execute
        service.transfer("1", "2", 50)

        // 3. verify
        assertEquals(150, senderAccount.balance)
        assertEquals(150, beneficiaryAccount.balance)
    }

    private class MockAccountManager : AccountManager {

        private val accounts: MutableMap<String, Account> = mutableMapOf()

        fun addAccount(userId: String, account: Account) {
            accounts[userId] = account
        }

        override fun findAccountForUser(userId: String): Account? = accounts[userId]

        override fun updateAccount(account: Account) {
            // do nothing
        }

    }

}