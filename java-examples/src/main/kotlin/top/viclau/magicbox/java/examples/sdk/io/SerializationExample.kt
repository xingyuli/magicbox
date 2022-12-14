package top.viclau.magicbox.java.examples.sdk.io

import top.viclau.magicbox.java.examples.absolutePathOf
import top.viclau.magicbox.java.examples.pathUnderPkg
import java.io.*

private fun serialize(o: Any, toPath: String) {
    with(File(toPath)) {
        if (exists()) {
            delete()
        }
        createNewFile()

        ObjectOutputStream(outputStream()).use {
            it.writeObject(o)
        }
    }
}

private inline fun <reified T : Any> deserialize(path: String): T {
    return ObjectInputStream(FileInputStream(path)).use {
        it.readObject() as T
    }
}

private class User(val firstname: String, val lastname: String) : Serializable {

    override fun toString(): String = "$firstname $lastname"

    companion object {
        private val serialPersistentFields = arrayOf(
            ObjectStreamField("firstname", String::class.java)
        )
    }

}

fun main() {
    val path: String =
        absolutePathOf(User::class.pathUnderPkg("user.bin"))!!

    println(path)

    serialize(User("Alex", "Cheng"), path)
    println(deserialize<User>(path))
}
