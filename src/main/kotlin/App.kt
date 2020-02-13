import net.dv8tion.jda.api.JDABuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import services.JDA
import services.GoogleSheets
import services.jda.CommandListener

object Application {
    fun start() {
        val logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)
        logger.info("Starting Services...")
        logger.debug("test")

        JDA.start()
        GoogleSheets.start()

        println("< JDA > \n" +
                "   > Name: ${JDA.service.selfUser.name} \n" +
                "   > Status: ${JDA.service.status} \n" +
                "   > Gateway Ping: ${JDA.service.gatewayPing} \n" +
                "   > Guilds: ${JDA.service.guilds.count()} \n")

        println("< Google Sheets > \n" +
                "   > Name: ${GoogleSheets.service.applicationName} \n" +
                "   > Base Url: ${GoogleSheets.service.baseUrl} \n" +
                "   > Root Url: ${GoogleSheets.service.rootUrl} \n")
    }
}

fun main() {
    Application.start()
}