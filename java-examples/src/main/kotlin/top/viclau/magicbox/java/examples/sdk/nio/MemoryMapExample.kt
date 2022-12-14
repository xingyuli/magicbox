package top.viclau.magicbox.java.examples.sdk.nio

import java.io.RandomAccessFile
import java.nio.channels.FileChannel

// 20 MB
private const val MEM_MAP_SIZE = 20 * 1024 * 1024
private const val FILENAME = "example_memory_mapped_file.txt"

/**
 * Source:
 * http://www.javaworld.com/javaworld/jw-10-2012/121016-maximize-java-nio-and-nio2-for-application-responsiveness.html?page=4
 */
fun main() {
    RandomAccessFile(FILENAME, "rw").use {
        // Mapping a file into memory
        val out = it.channel.map(FileChannel.MapMode.READ_WRITE, 0, MEM_MAP_SIZE.toLong())

        // Writing into Memory Mapped File
        for (i in 0 until MEM_MAP_SIZE) {
            out.put('A'.code.toByte())
        }
        println("File '$FILENAME' is now $MEM_MAP_SIZE bytes full.")

        // Read from memory-mapped file.
        for (i in 0..29) {
            println(Char(out[i].toUShort()))
        }
        println("Reading from memory-mapped file '$FILENAME' is complete.")
    }
}
