package top.viclau.magicbox.java.examples.sdk.nio

import java.nio.ByteBuffer

private fun cafebabe() {
    val buff = ByteBuffer.allocate(8)

    // 0x cafe babe
    buff.put(0xca.toByte())
    buff.putShort(0xfeba.toShort())
    buff.put(0xbe.toByte())

    buff.flip()

    val data = buff.int

    // prints "cafebabe"
    println(Integer.toHexString(data))
}

private fun wrap() {
    val internal = "Hello World!".toByteArray()

    // will share the same underlying data array
    val buffer = ByteBuffer.wrap(internal)
    println(String(buffer.array()))

    internal[internal.size - 1] = '.'.code.toByte()
    // prints "Hello World."
    println(String(buffer.array()))
}

private fun slice() {
    // will produce 8 bytes
    val data = "Java NIO".toByteArray()

    // limit = capacity = array.length
    // position = 0
    val buffer = ByteBuffer.wrap(data)

    // window from [5, 8)
    buffer.position(5)
    val sliceOfBuffer = buffer.slice()

    // buffer produces by slice will have independent variables
    println(sliceOfBuffer.position()) // 0
    println(sliceOfBuffer.limit())    // 3
    println(sliceOfBuffer.capacity()) // 3
    // "NIO"
    println(String(sliceOfBuffer.array(), 5, data.size - 5))

    // but they still share the same underlying data array
    println(sliceOfBuffer.array() == buffer.array())
}

fun main() {
    // cafebabe()
    // wrap()
    slice()
}
