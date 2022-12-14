package top.viclau.magicbox.java.examples.sdk.enumeration

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket

enum class Weekday {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}

object EnumServer : Runnable {
    override fun run() {
        ServerSocket(1234).use {
            val socket = it.accept()
            val `in` = ObjectInputStream(socket.getInputStream())
            val day: Weekday = `in`.readObject() as Weekday
            if (day === Weekday.FRIDAY) {
                println("is same object")
            } else {
                println("isn't same object")
            }

            when (day) {
                Weekday.FRIDAY -> println("switch-case is FRIDAY")
                else -> println("switch-case failed")
            }
        }
    }
}

object EnumClient : Runnable {
    override fun run() {
        Socket("localhost", 1234).use {
            val out = ObjectOutputStream(it.getOutputStream())
            out.writeObject(Weekday.FRIDAY)
            out.flush()
        }
    }
}

fun main() {
    val server = Thread(EnumServer).apply { start() }
    val client = Thread(EnumClient).apply { start() }

    server.join()
    client.join()
}
