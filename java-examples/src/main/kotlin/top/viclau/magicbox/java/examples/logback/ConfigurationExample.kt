package top.viclau.magicbox.java.examples.logback

import org.slf4j.LoggerFactory

class ConfigurationExample {

    private val logger = LoggerFactory.getLogger(ConfigurationExample::class.java)

    fun levelExample() {
        logger.info("Entering application.")

        // won't be logged as the level of package o.s.t.j.s.Chapters.configuration is set to INFO
        logger.debug("just do something...")

        logger.info("Exiting application.")
    }

    fun variableExample() {
        logger.info("something happened")
    }

}

fun main() {
//     runLevelExample()
    runVariableExample()
}

private fun runLevelExample() {
     System.getProperties().setProperty("logback.configurationFile", "top/viclau/magicbox/java/examples/logback/configuration-example-level.xml")
     ConfigurationExample().levelExample()
}

private fun runVariableExample() {
    System.getProperties().setProperty("logback.configurationFile", "top/viclau/magicbox/java/examples/logback/configuration-example-variable.xml")
    ConfigurationExample().variableExample()
}
