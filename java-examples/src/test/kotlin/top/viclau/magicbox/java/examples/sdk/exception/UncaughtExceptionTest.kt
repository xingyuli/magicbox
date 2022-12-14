package top.viclau.magicbox.java.examples.sdk.exception

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class UncaughtExceptionTest {

    @Test
    fun withinExecutorTest() {
        val executorService = Executors.newFixedThreadPool(5) as ThreadPoolExecutor

        // the exception will be recorded in FutureTask.outcome, just like a normal result
        // thus won't impact the executing Thread
        val future = executorService.submit<Void> {
            println("do sth.")
            throw IllegalArgumentException("ex demo")
        }

        // wait for task completion
        Thread.sleep(1000)

        // task with exception still be counted as completed
        Assertions.assertEquals(1, executorService.completedTaskCount, "1 submitted task should be completed")

        Assertions.assertTrue(future.isDone, "future treated as done")

        try {
            // the exception will be reported when `get()`
            future.get()
            Assertions.fail("won't be reached")
        } catch (e: ExecutionException) {
            MatcherAssert.assertThat(e.cause, CoreMatchers.instanceOf(IllegalArgumentException::class.java))
            Assertions.assertEquals("ex demo", e.cause!!.message)
        }

        Assertions.assertEquals(1, executorService.poolSize, "thread num in pool will not be impacted by exception")

        executorService.shutdown()
        executorService.awaitTermination(5, TimeUnit.SECONDS)
    }

}