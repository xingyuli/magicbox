/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples

import java.io.File
import kotlin.reflect.KClass

class ClasspathResources

fun absolutePathOf(filePath: String, createIfNotFound: Boolean = false): String? {
    val resource = ClasspathResources::class.java.classLoader.getResource(filePath)

    if (resource == null && createIfNotFound) {
        return File(filePath).let {
            it.parentFile?.mkdirs()

            it.createNewFile()
            it.absolutePath
        }
    }

    return resource?.path
}

fun <T : Any> KClass<T>.pathUnderPkg(filename: String): String {
    val dir = java.`package`.name.replace(".", File.separator)
    return "${dir}${File.separator}$filename"
}
