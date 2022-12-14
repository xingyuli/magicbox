package top.viclau.magicbox.java.examples.sdk.io

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import top.viclau.magicbox.java.examples.absolutePathOf
import top.viclau.magicbox.java.examples.pathUnderPkg
import top.viclau.magicbox.java.examples.sdk.io.InputStreamProvider.InputStreamUser
import java.io.*


@Disabled("Out of dated since these test cases are very poor..., thus cannot be used as real test!")
class InputStreamTest {

    @Test
    fun testUsedByOneUser() {
        val inProvider = InputStreamProvider(absolutePathOf(InputStreamTest::class.pathUnderPkg("read.txt"))!!)
        inProvider.usedBy(LineNumberedUser())
    }

    @Test
    fun testUsedByMultipleUsers() {
        val inUsers: MutableCollection<InputStreamUser> = ArrayList()
        inUsers.add(LineNumberedUser())
        inUsers.add(object : InputStreamUser {
            override fun use(`in`: InputStream) {
                val reader = BufferedReader(InputStreamReader(`in`))
                val content = StringBuilder()
                var line: String?
                while (null != reader.readLine().also { line = it }) {
                    line = line!!.replace('a', 'A')
                        .replace('e', 'E')
                        .replace('i', 'I')
                        .replace('o', 'O')
                        .replace('u', 'U')
                    content.append(line)
                    content.append('\n')
                }
                println(content)
            }
        })
        val inProvider = InputStreamProvider(absolutePathOf(InputStreamTest::class.pathUnderPkg("read.txt"))!!)
        inProvider.usedBy(inUsers)
    }

    private class LineNumberedUser : InputStreamUser {
        override fun use(`in`: InputStream) {
            val reader = LineNumberReader(BufferedReader(InputStreamReader(`in`)))
            var line: String?
            while (null != reader.readLine().also { line = it }) {
                println(reader.lineNumber.toString() + ": " + line)
            }
        }
    }

}