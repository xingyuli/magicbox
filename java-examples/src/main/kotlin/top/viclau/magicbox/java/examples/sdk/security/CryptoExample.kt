/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.security

import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature


private fun dsaSign(data: ByteArray, privateKey: PrivateKey): ByteArray {
    val signatureForSign = Signature.getInstance("SHA1withDSA")

    // a private key is needed when sign
    signatureForSign.initSign(privateKey)

    signatureForSign.update(data)

    // got the signature
    return signatureForSign.sign()
}

private fun dsaVerify(data: ByteArray, sign: ByteArray, publicKey: PublicKey): Boolean {
    val signatureForVerify = Signature.getInstance("SHA1withDSA")

    signatureForVerify.initVerify(publicKey)

    signatureForVerify.update(data)

    return signatureForVerify.verify(sign)
}

fun main() {
    val keyGenerator = KeyPairGenerator.getInstance("DSA")
    keyGenerator.initialize(1024)

    val keyPair = keyGenerator.generateKeyPair()

    val data = "Hello World".toByteArray()
    val sign = dsaSign(data, keyPair.private)

    // the public key for verifying the signature can be transferred
    // as a file
    val publicKey = keyPair.public

    // send the public key to ...

    // assumed that we've already got the public key somehow
    println(dsaVerify(data, sign, publicKey))
}
