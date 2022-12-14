package top.viclau.magicbox.java.examples.sdk.io

/**
 * Compare the equality of the provided `src` byte array and
 * `dest` byte array.
 *
 *
 * The comparison will begin at the start point of the provided array. The
 * start points of the `src` and `dest` array are
 * specified by the `srcOffset` and `destOffset`,
 * respectively. The number of elements to be compared is specified by the
 * `length`.
 *
 *
 * NOTE:
 *
 *  * two <tt>null</tt> arrays are considered equal
 *  * two empty arrays are considered equal
 *
 *
 * @param src
 * the source array, can be null
 * @param srcOffset
 * the start point of the source array, inclusive
 * @param dest
 * the destination array, can be null
 * @param destOffset
 * the start point of the destination array, inclusive
 * @param length
 * the number of elements to be compared
 * @return <tt>true</tt> if the two arrays are equal
 * @throws ArrayIndexOutOfBoundsException
 * if srcOffset &lt; 0 || srcOffset &gt;= src.length or
 * destOffset &lt; 0 || destOffset &gt;= dest.length
 * @throws IllegalArgumentException
 * if length &lt;= 0 or
 * srcOffset + length &gt; src.length or
 * destOffset + length &gt; dest.length
 */
fun equals(
    src: ByteArray?, srcOffset: Int,
    dest: ByteArray?, destOffset: Int, length: Int
): Boolean {
    if (null == src && null == dest) {
        return true
    }
    if (null == src || null == dest) {
        return false
    }
    if (src.isEmpty() && dest.isEmpty()) {
        return true
    }
    if (src.isEmpty() || dest.isEmpty()) {
        return false
    }

    if (srcOffset < 0 || srcOffset >= src.size) {
        throw ArrayIndexOutOfBoundsException(srcOffset)
    }
    if (destOffset < 0 || destOffset >= dest.size) {
        throw ArrayIndexOutOfBoundsException(destOffset)
    }

    require(length > 0) { "length should be larger than 0" }
    require(srcOffset + length <= src.size) { "length exceed the upper bounds of src when starting from the offset $srcOffset" }
    require(destOffset + length <= dest.size) { "length exceed the upper bounds of dest when starting from the offset $destOffset" }

    for (i in 0 until length) {
        if (src[srcOffset + i] != dest[destOffset + i]) {
            return false
        }
    }

    return true
}