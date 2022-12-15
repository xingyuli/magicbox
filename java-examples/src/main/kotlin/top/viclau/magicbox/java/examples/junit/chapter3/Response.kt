/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.junit.chapter3

interface Response {
    val name: String
}

class ErrorResponse(val originalRequest: Request, val originalException: Exception) : Response {

    override val name: String
        get() = "Error"

}
