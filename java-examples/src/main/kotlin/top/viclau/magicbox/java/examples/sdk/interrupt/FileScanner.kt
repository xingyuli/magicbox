package top.viclau.magicbox.java.examples.sdk.interrupt

import java.io.File
import java.util.*

private fun listFile(f: File) {
    if (f.isFile) {
        println(f)
        // Thread.sleep(200)
        return
    }

    val allFiles = f.listFiles()
    if (Thread.interrupted()) {
        throw InterruptedException("File scanning interrupted")
    }

    allFiles?.let {
        for (file in it) {
            listFile(file)
        }
    }
}

private fun readFromConsole(): String {
    val scanner = Scanner(System.`in`)
    return try {
        scanner.nextLine()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun main() {
    val fileIteratorThread = object : Thread() {
        override fun run() {
            try {
                listFile(File("/Users/viclau"))
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    object : Thread() {
        override fun run() {
            while (true) {
                if ("quit".equals(readFromConsole(), ignoreCase = true)) {
                    if (fileIteratorThread.isAlive) {
                        fileIteratorThread.interrupt()
                        return
                    }
                } else {
                    println("Type in 'quit' to exit the file scanning")
                }
            }
        }
    }.start()

    fileIteratorThread.start()
}
