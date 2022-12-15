/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.ref

import java.lang.ref.SoftReference
import java.sql.ResultSet
import java.util.*

/**
 * Source: https://www.kdgregory.com/index.php?page=java.refobj
 */
fun main() {
    /*
     * You must hold a strong reference to the reference object.
     *
     * If you create a reference object, but allow it to go out of scope,
     * then the reference object itself will be garbage collected. Seems
     * obvious, but it's easy to forget, particularly when you're using
     * reference queues to track when the reference objects get cleared.
     */
    val ref = SoftReference<MutableList<Stuff>>(LinkedList())
    for (i in 0..4) {
        val referent = ref.get()
        referent?.add(Stuff("demo1-$i"))
    }

    /*
     * You must always check to see if the referent is null.
     *
     * The garbage collector can clear the reference at any time, and if
     * you blithely use the reference, sooner or later you'll get
     * NullPointerException.
     */
    val list = ref.get() ?: throw RuntimeException("ran out of memory")

    /*
     * You must hold a strong reference to the referent to use it.
     *
     * Again, the garbage collector can clear the reference at any time,
     * even between two statements in your code. If you simply call get()
     * once to check for null, and then call get() again to use the
     * reference, it might be cleared between those calls.
     *
     * So, the right way is,
     *   first hold a strong reference to the returned value of the get(),
     *   then check the null-ability against that strong reference,
     *   if not null, then you can operate on that strong reference now.
     */
    list.add(Stuff("demo1-last"))
}

/*
 * Process query results in a generic way and ensure the ResultSet is
 * properly closed. It only has one small flaw: what happens if the
 * query returns a million rows?
 *
 * The answer, of course, is an OutOfMemoryError, unless you have a
 * gigantic heap or tiny rows. It's the perfect place for a circuit
 * breaker: if the JVM runs out of memory while processing the query,
 * release all the memory that it's already used, and throws an
 * application-specific exception.
 *
 * At this point, you may wonder: who cares? The query is going to
 * abort in either case, why not just let the out-of-memory error do
 * the job? The answer is that your application may not be the only
 * thing affected. If you're running on an application server, your
 * memory usage could take down other applications. Even in an unshared
 * environment, a circuit-breaker improves the robustness of your
 * application, because it confines the problem and gives you a chance
 * to recover and continue.
 */
private fun processResults(rslt: ResultSet): List<List<Any>> = try {
    val results: MutableList<List<Any>> = LinkedList()
    val meta = rslt.metaData
    val colCount = meta.columnCount
    while (rslt.next()) {
        val row: MutableList<Any> = ArrayList(colCount)
        for (i in 1..colCount) {
            row.add(rslt.getObject(i))
        }
        results.add(row)
    }
    results
} finally {
    rslt.close()
}

fun processResultsUsingSoftReference(rslt: ResultSet): List<List<Any>>? {
    /*
     * To create the circuit-breaker, the first thing you need to do is
     * wrap the results list in a SoftReference.
     */
    val ref = SoftReference<MutableList<List<Any>>>(LinkedList())
    val meta = rslt.metaData
    val colCount = meta.columnCount
    var rowCount = 0
    while (rslt.next()) {
        rowCount++
        val row: MutableList<Any> = ArrayList()
        for (i in 1..colCount) {
            row.add(rslt.getObject(i))
        }

        /*
         * And then, as you iterate through the results, create a strong
         * reference to the list only when you need to update it.
         */
        var results = ref.get()
        results?.add(row) ?: throw TooManyResultsException(rowCount)

        /*
         * Note that the results variable is set to null after adding the
         * new element -- this is one of the few cases where doing so is
         * justified. Although the variables go out of scope at the end
         * of loop, the garbage collector does not know that (because
         * there's no reason for the JVM to clear the variable's slot in
         * the call stack). So, if not clear the variable, it would be an
         * unintended strong reference during the subsequent pass through
         * the loop.
         */
        results = null
    }

    return ref.get()
}

private class Stuff(val id: String) {
    protected fun finalize() {
        println("finalize $id")
    }
}

private class TooManyResultsException(count: Int) : Exception(count.toString())
