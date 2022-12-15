/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.jwt

import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import io.jsonwebtoken.Jwts


private fun parseWithJjwt(token: String) {
    // https://github.com/jwtk/jjwt/issues/280
    val i = token.lastIndexOf('.')
    val tokenWithoutSignature = token.substring(0, i + 1)
    val untrusted = Jwts.parserBuilder().build().parseClaimsJwt(tokenWithoutSignature)
    println(untrusted.body)
}

private fun parseWithJoseJwt(token: String) {
    val jwsObject: JWSObject = JWSObject.parse(token)
    println(jwsObject)

    val payload: Payload = jwsObject.payload
    println(payload.toJSONObject())
}

fun main() {
    // You can get a token from: https://jwt.io/
    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

    // cannot parse expired token
    parseWithJjwt(token);

    parseWithJoseJwt(token)
}
