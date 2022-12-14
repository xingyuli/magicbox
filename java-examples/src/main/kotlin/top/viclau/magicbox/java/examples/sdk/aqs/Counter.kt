package top.viclau.magicbox.java.examples.sdk.aqs

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger


class AtomicCounter {

    private val value = AtomicInteger()

    fun getValue(): Int = value.get()

    fun increment() {
        // this is just a demonstration, in fact AtomicInteger provides
        // getAndIncrement() for us
        while (true) {
            val current = value.get()
            val next = current + 1
            if (value.compareAndSet(current, next)) {
                break
            }
        }
    }

}

private var concurrencyLevel = 0

private fun execute(counter: AtomicCounter) {
    val ready = CountDownLatch(concurrencyLevel)
    val go = CountDownLatch(1)
    val done = CountDownLatch(concurrencyLevel)
    for (i in 0 until concurrencyLevel) {
        object : Thread() {
            override fun run() {
                ready.countDown()
                try { go.await() } catch (_: InterruptedException) {}
                counter.increment()
                done.countDown()
            }
        }.start()
    }

    try { ready.await() } catch (_: InterruptedException) {}
    go.countDown()
    try { done.await() } catch (_: InterruptedException) {}
}

fun main() {
    concurrencyLevel = 50
    val atomicCounter = AtomicCounter()
    execute(atomicCounter)
    println(
        "atomicCounter.value == " + concurrencyLevel + ": "
                + (concurrencyLevel == atomicCounter.getValue())
    )
}
