package top.viclau.magicbox.java.examples.junit.chapter7

class WebClient {

    fun getContent(connectionFactory: ConnectionFactory): String? {
        try {
            connectionFactory.getData().use { `is` ->
                val content = StringBuffer()
                var data: Int
                while (-1 != `is`.read().also { data = it }) {
                    content.append(Character.toChars(data))
                }
                return content.toString()
            }
        } catch (e: Exception) {
            return null
        }
    }

}