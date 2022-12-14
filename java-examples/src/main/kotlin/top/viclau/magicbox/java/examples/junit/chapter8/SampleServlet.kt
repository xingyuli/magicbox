package top.viclau.magicbox.java.examples.junit.chapter8

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest

class SampleServlet : HttpServlet() {

    fun isAuthenticated(req: HttpServletRequest): Boolean {
        val session = req.getSession(false) ?: return false
        val authenticationAttribute = session.getAttribute("authenticated") as String
        return authenticationAttribute.toBoolean()
    }

}