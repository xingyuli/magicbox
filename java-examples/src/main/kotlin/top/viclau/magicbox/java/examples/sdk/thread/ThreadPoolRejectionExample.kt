/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.thread

import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.*
import java.util.function.Supplier

fun main() {
    val poolSize = 2
    val queueSize = 64

    // the AbortPolicy will populate the RejectedExecutionException to the caller,
    //   - if this exception is not caught, then the caller thread will be propagated; the successfully submitted tasks might be halt as well
    //   - if this exception is caught, then the caller can still reach the end; the successfully submitted tasks could be handled properly
    runTasksWith("use (default) AbortPolicy", poolSize, queueSize) {
        useDefaultRejectedHandler(
            poolSize,
            queueSize
        )
    }

    // no special setup needed, the task will always be completed
    runTasksWith("use CallerRunsPolicy", poolSize, queueSize) {
        usePolicy(poolSize, queueSize, object : ThreadPoolExecutor.CallerRunsPolicy() {
            override fun rejectedExecution(r: Runnable, e: ThreadPoolExecutor) {
                println("task handled by caller")
                super.rejectedExecution(r, e)
            }
        })
    }

    // DiscardPolicy will leads to task timeout, which impact the `Future.get()`.
    // `Future.get(timeout: Long, unit: TimeUnit)` will survive this.
    //   - if `Future.get()` is used, then the caller thread would be halt, as the rejected task will never be completed
    //   - if `Future.get(timeout, unit)` is used, then the caller thread could be notified by the `TimeoutException`
    runTasksWith("use DiscardPolicy", poolSize, queueSize) {
        usePolicy(poolSize, queueSize, object : ThreadPoolExecutor.DiscardPolicy() {
            override fun rejectedExecution(r: Runnable, e: ThreadPoolExecutor) {
                println("task discarded")
                super.rejectedExecution(r, e)
            }
        })
    }
}

private fun runTasksWith(desc: String, poolSize: Int, queueSize: Int, supplier: Supplier<ThreadPoolExecutor>) {
    val threadPoolExecutor = supplier.get()

    println(">>> $desc <<<")

    try {
        val e = runTasks(desc, poolSize, queueSize, threadPoolExecutor)

        if (e == null) {
            println("[$desc] reaches the end")
        } else {
            println("[$desc] didn't reach the end, the cause is: ${stackTraceToString(e)}")
        }

    } finally {
        println("[$desc] ${threadPoolExecutor.completedTaskCount} tasks completed")

        if (!threadPoolExecutor.isShutdown) {
            println("[$desc] need extra shutdown")
            threadPoolExecutor.shutdownNow()
        }

        println()
    }
}

private fun runTasks(desc: String, poolSize: Int, queueSize: Int, threadPoolExecutor: ThreadPoolExecutor): Exception? {
    val futures: MutableList<Future<*>> = ArrayList()
    val latch = CountDownLatch(queueSize)

    val triggerMaxQueSize = queueSize + poolSize + 1

    try {
        for (i in 0 until triggerMaxQueSize) {
            try {
                futures.add(threadPoolExecutor.submit {
                    try {
                        latch.await()

                        // workload
                        Thread.sleep(10)
                    } catch (e: InterruptedException) {
                        println("[$desc] interrupted when Runnable.run, the cause is: ${stackTraceToString(e)}")
                    }
                })
            } catch (e: RejectedExecutionException) {
                println("[$desc] interrupted when ThreadPoolExecutor.submit, the cause is: ${stackTraceToString(e)}")
            }
            latch.countDown()
        }
    } catch (e: Exception) {
        return e
    }

    println("[$desc] ${futures.size} tasks submitted")

    for (i in futures.indices) {
        val f = futures[i]
        try {
            f.get(5, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            println("[$desc] i = $i, interrupted when Future.get, the cause is: ${stackTraceToString(e)}")
        } catch (e: ExecutionException) {
            println("[$desc] i = $i, interrupted when Future.get, the cause is: ${stackTraceToString(e)}")
        } catch (e: TimeoutException) {
            println("[$desc] i = $i, timeout when Future.get, the cause is: ${stackTraceToString(e)}")
        }
    }

    threadPoolExecutor.shutdownNow()

    return null
}

private fun useDefaultRejectedHandler(poolSize: Int, queueSize: Int): ThreadPoolExecutor {
    // the default handler is AbortPolicy
    return ThreadPoolExecutor(
        poolSize, poolSize, 60, TimeUnit.SECONDS,
        LinkedBlockingDeque(queueSize)
    )
}

private fun usePolicy(poolSize: Int, queueSize: Int, handler: RejectedExecutionHandler): ThreadPoolExecutor {
    return ThreadPoolExecutor(
        poolSize, poolSize, 60, TimeUnit.SECONDS,
        LinkedBlockingDeque(queueSize), Executors.defaultThreadFactory(), handler
    )
}

private fun stackTraceToString(e: Exception): String {
    val string = StringWriter()
    val writer = PrintWriter(string)
    e.printStackTrace(writer)
    return string.toString()
}
