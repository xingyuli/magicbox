package top.viclau.magicbox.java.examples.logback

import org.slf4j.LoggerFactory

class SMTPAppenderExample {

    private val logger = LoggerFactory.getLogger(SMTPAppenderExample::class.java)

    fun run() {
        for (i in 0..9) {
            logger.debug("debug message from application")
        }
        logger.warn("this is a warning message")
        logger.error("this is an error message")
    }

}

fun main() {
    System.getProperties().setProperty("logback.configurationFile", "top/viclau/magicbox/java/examples/logback/smtp-appender-example.xml")
    SMTPAppenderExample().run()
}
