/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.junit.chapter6

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class WebClient {

    fun getContent(url: URL): String? {
        val content = StringBuffer()
        try {
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            val `is` = connection.inputStream
            val buffer = ByteArray(1024)
            var count: Int
            while (-1 != `is`.read(buffer).also { count = it }) {
                content.append(String(buffer, 0, count))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return content.toString()
    }

}
