package top.viclau.magicbox.java.examples.sdk.collection

import java.util.*

class SetAdapter<E>(private val s: MutableSet<E>) : MutableSet<E> by s {

    fun append(e: E): SetAdapter<E> {
        s.add(e)
        return this
    }

    fun unmodifiableSet(): Set<E> = Collections.unmodifiableSet(s)

}

fun main() {
    useStandardSet()
}

private fun useStandardSet() {
    val temp = mutableSetOf<String>()
    temp.add("\b(roast beef)\b")
    temp.add("\b(on rye)\b")
    temp.add("\b(with mustard)\b")

    val patterns = Collections.unmodifiableSet(temp)
}

private fun useSetAdapter() {
    val patterns = SetAdapter(mutableSetOf<String>())
        .append("\b(roast beef)\b")
        .append("\b(on rye)\b")
        .append("\b(with mustard)\b")
        .unmodifiableSet()
}
