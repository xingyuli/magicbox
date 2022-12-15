/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.nio

import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction

private fun printDefaultCharset() {
    System.getProperties().list(System.out)
    println("default charset: ${Charset.defaultCharset()}")
}

private fun encodeAndDecode() {
    val charset = Charset.forName("ISO-8859-1")

    val encoder = charset.newEncoder()
    // this will filter out characters which cannot be recognized by the
    // ISO-8859-1 charset
    encoder.onUnmappableCharacter(CodingErrorAction.IGNORE)

    val decoder = charset.newDecoder()

    val input = "你123好"
    val buffer = CharBuffer.allocate(32)
    buffer.put(input)
    buffer.flip()

    try {
        // encode: characters -> bytes
        val byteBuffer = encoder.encode(buffer)

        // decode: bytes -> characters
        val cbuf = decoder.decode(byteBuffer)

        // as unmappable characters are IGNOREd, the output is "123"
        println(cbuf)
    } catch (e: CharacterCodingException) {
        e.printStackTrace()
    }
}

fun main() {
    printDefaultCharset()

    encodeAndDecode()
}
