package top.viclau.magicbox.java.examples.junit.chapter7

class Account(private val accountId: String, balance: Long) {

    private var _balance: Long

    init {
        _balance = balance
    }

    fun debit(amount: Long) {
        _balance -= amount
    }

    fun credit(amount: Long) {
        _balance += amount
    }

    val balance: Long
        get() = _balance

}

interface AccountManager {
    fun findAccountForUser(userId: String): Account?
    fun updateAccount(account: Account)
}

class AccountService {

    private lateinit var accountManager: AccountManager

    fun setAccountManager(accountManager: AccountManager) {
        this.accountManager = accountManager
    }

    fun transfer(senderId: String, beneficiaryId: String, amount: Long) {
        val sender = accountManager.findAccountForUser(senderId)
        val beneficiary = accountManager.findAccountForUser(beneficiaryId)
        sender!!.debit(amount)
        beneficiary!!.credit(amount)
        accountManager.updateAccount(sender)
        accountManager.updateAccount(beneficiary)
    }

}