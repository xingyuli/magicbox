package top.viclau.magicbox.java.examples.sdk.ref

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * Source:
 * http://www.kdgregory.com/index.php?page=java.refobj
 * <p>
 *
 * What are "roots"?
 * <p>
 *
 * In a simple Java application, they're method parameters and local variables
 * stored on the stack, the operands of the currently executing expression(also
 * stored on the stack), and static class member variables.
 * <p>
 *
 * <b>
 * NOTE:
 * Whether or not a strongly reference depends on whether can be referenced by a
 * certain reference chain start from a root reference, it has no relationship
 * with the reference type!
 * </b>
 * <p>
 *
 * It's important to understand root references, because they define what a
 * "strong" reference is: if you can follow a chain of references from a root to
 * a particular object, then that object is "strongly" referenced.
 */
// @Disabled("This is not a real test!")
class MyReferenceTest {

    @Test
    fun gcWhenStronglyReachable() {
        val strongS = Stuff("Strongly Reachable")
        System.gc()
        assertNotNull(strongS)
    }

    @Test
    fun gcWhenSoftlyReachable() {
        /*
		 * The garbage collector will attempt to preserve the object as long as
		 * possible, but will collect it before throwing an OutOfMemoryError.
		 */
        val softS = SoftReference(Stuff("Softly Reachable"))
        System.gc()
        assertNotNull(softS.get())
    }

    @Test
    fun gcWhenWeaklyReachable() {
        /*
		 * The garbage collector is free to collect the object at any time,
		 * with no attempt to preserve it. In practice, the object will be
		 * collected during a major collection, but may survive a minor
		 * collection.
		 */
        val weakS = WeakReference(Stuff("Weakly Reachable"))
        System.gc()
        assertNull(weakS.get())
    }

    @Test
    fun gcWhenPhantomReachable() {
        /*
		 * This reference type differs from the other two in that it isn't
		 * meant to be used to access the object, but as a signal that the
		 * object has already been finalized, and the garbage collector is
		 * ready to reclaim its memory.
		 */
        val queue = ReferenceQueue<Stuff>()
        val phantomS = PhantomReference(Stuff("Phantom Reachable"), queue)

        Thread {
            try {
                println("Awaiting for GC")
                val ref = queue.remove() as PhantomReference<*>
                println(ref)
                println("Stuff has been enqueued!")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()

        TimeUnit.SECONDS.sleep(2)

        println("Invoking GC")
        System.gc()
    }

    @Test
    fun gcWhenUnreachable() {
        Stuff("Unreachable")
        System.gc()
    }

    private class Stuff(val id: String) {
        protected fun finalize() {
            println("finalize $id")
        }
    }

}