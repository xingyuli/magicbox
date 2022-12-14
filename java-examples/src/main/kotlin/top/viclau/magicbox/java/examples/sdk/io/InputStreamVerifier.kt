package top.viclau.magicbox.java.examples.sdk.io

import top.viclau.magicbox.java.examples.sdk.io.InputStreamProvider.InputStreamUser
import java.io.InputStream


/**
 * Constructs a verifier with the given bytes.
 *
 *
 * NOTE: Any changes to the `expectedData` array will impact this
 * verifier as the verifier simply references the same array.
 *
 * @throws IllegalArgumentException
 * if expectedData is not valid
 * @param expectedData
 * expected bytes of data, should not be null
 */
class InputStreamVerifier(private val expectedData: ByteArray) : InputStreamUser {

    private var isMatched = false

    override fun use(`in`: InputStream) {
        val buf = ByteArray(512)
        var offset = 0
        var bytesRead: Int
        while (-1 != `in`.read(buf, 0, buf.size).also { bytesRead = it }) {
            if (!equals(expectedData, offset, buf, 0, bytesRead)) {
                isMatched = false
                return
            }
            offset += bytesRead
        }
        isMatched = true
    }

}