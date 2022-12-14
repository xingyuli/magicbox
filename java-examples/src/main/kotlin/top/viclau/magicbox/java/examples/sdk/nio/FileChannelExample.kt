package top.viclau.magicbox.java.examples.sdk.nio

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.file.FileAlreadyExistsException
import java.util.*

fun read(path: String): String = FileInputStream(path).use { inStream ->
    val inChannel = inStream.channel

    // allocate the bytes on the Heap
    val buffer = ByteBuffer.allocate(256)
    val dst = ByteArray(256)

    val zero: Byte = 0
    var bytesRead: Int
    val content = StringBuilder()
    while (-1 != inChannel.read(buffer).also { bytesRead = it }) {
        buffer.position(0)
        buffer.get(dst)
        buffer.clear()

        content.append(String(dst, 0, bytesRead))
        Arrays.fill(dst, zero)
    }

    return content.toString()
}

fun read2(path: String): String = FileInputStream(path).use { inStream ->
    val inChannel = inStream.channel

    val buffer = ByteBuffer.allocate(1024)
    val dst = ByteArray(1024)

    val content = StringBuilder()
    while (inChannel.read(buffer) > 0) {
        buffer.flip()
        buffer.get(dst, 0, buffer.limit())

        content.append(String(dst, 0, buffer.limit()))
        buffer.clear()
    }

    return content.toString()
}

fun write(content: String, toPath: String, overwrite: Boolean = true) {
    val f = File(toPath).onExists(overwrite = true)

    FileOutputStream(f).use { outStream ->
        val outChannel = outStream.channel
        val buf = ByteBuffer.wrap(content.toByteArray())
        while (buf.hasRemaining()) {
            outChannel.write(buf)
        }
    }
}

fun writeFromHttpUrl(httpUrl: String, toPath: String, overwrite: Boolean = true) {
    URL(httpUrl).openStream().use { inStream ->
        val readChannel = Channels.newChannel(inStream)

        val outFile = File(toPath).onExists(overwrite = true)

        FileOutputStream(outFile).use { outStream ->
            outStream.channel.transferFrom(readChannel, 0, Int.MAX_VALUE.toLong())
        }
    }
}

private fun File.onExists(overwrite: Boolean): File {
    if (exists()) {
        if (overwrite) {
            delete()
            createNewFile()
        } else {
            throw FileAlreadyExistsException(path)
        }
    }
    return this
}
