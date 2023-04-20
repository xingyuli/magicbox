/*
 * Copyright (c) 2023 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.box.client.http.superset.security

data class LoginRequest(val password: String, val provider: String, val refresh: Boolean, val username: String) {
    data class Response(val access_token: String, val refresh_token: String?)
}
