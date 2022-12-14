package top.viclau.magicbox.java.examples.sdk.io

import java.io.*
import java.util.logging.Level
import java.util.logging.Logger


class InputStreamProvider(path: String) {

    private val path: String

    /**
     * Constructs a stream provided with the given file path.
     *
     * @param path
     * path of the file, should not be null or empty string
     * @throws IllegalArgumentException
     * if the path is not valid
     */
    init {
        require(path.isNotBlank()) { "path should be non-empty string" }
        if (!File(path).exists()) {
            throw FileNotFoundException(path)
        }
        this.path = path
    }

    /**
     * Consume the stream which provided by this InputStreamProvider with the
     * specified user(client).
     *
     *
     * NOTE:
     *
     *  * This method closes the stream implicitly which means the user(client) can leave the stream open safely in its
     *  code.
     *  * This method handles correctly even if the user(client) choose to close the stream by itself.
     *
     *
     * @param inUser
     * the user(client) who want to use the stream, should not be
     * null
     */
    fun usedBy(inUser: InputStreamUser) {
        var `in`: InputStream? = null
        try {
            LOG.log(Level.INFO, "opening InputStream for $path ...")
            `in` = FileInputStream(path)
            inUser.use(`in`)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != `in`) {
                try {
                    LOG.log(Level.INFO, "closing InputStream ...")
                    `in`.close()
                    LOG.log(Level.INFO, "InputStream closed")
                } catch (e: IOException) {
                    LOG.log(Level.WARNING, "cannot close InputStream", e)
                }
            }
        }
    }

    /**
     * Consume the stream which provided by this InputStreamProvider with the
     * specified users(clients). The users(clients) will be used sequentially
     * according to the iteration order.
     *
     * @see .usedBy
     * @param inUsers
     * the users(clients) who want to use the stream, should not be
     * null
     */
    fun usedBy(inUsers: Collection<InputStreamUser>) {
        var `in`: BufferedInputStream? = null
        try {
            LOG.log(
                Level.INFO,
                "opening BufferedInputStream for $path ..."
            )
            `in` = BufferedInputStream(FileInputStream(path))
            for (inUser in inUsers) {
                `in`.mark(Int.MAX_VALUE)
                inUser.use(`in`)
                `in`.reset()
                LOG.log(Level.INFO, "stream has been reset")
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != `in`) {
                try {
                    LOG.log(Level.INFO, "closing BufferedInputStream ...")
                    `in`.close()
                    LOG.log(Level.INFO, "BufferedInputStream closed")
                } catch (e: IOException) {
                    LOG.log(Level.WARNING, "cannot close BufferedInputStream", e)
                }
            }
        }
    }

    /**
     * Indicates a client to use a certain InputStream. This client has no
     * responsibility to close the stream passed in as it will always be closed
     * by [InputStreamProvider].
     */
    interface InputStreamUser {
        /**
         * Specify how to use the stream passed in.
         *
         * @param in
         * @throws IOException
         */
        @Throws(IOException::class)
        fun use(`in`: InputStream)
    }

    companion object {
        private val LOG = Logger.getLogger("input-stream-provider")
    }

}