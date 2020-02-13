package services

import JDAConstants
import net.dv8tion.jda.api.JDA
import services.jda.commands.Command
import net.dv8tion.jda.api.JDABuilder
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.slf4j.LoggerFactory
import services.jda.CommandListener
import java.io.FileNotFoundException
import java.io.InputStreamReader

object JDA {
    val logger = LoggerFactory.getLogger("Application")

    lateinit var service: JDA
    var commands = mutableMapOf<String, Command>()

    fun start() {
        logger.info("Starting JDA...")

        val inputStream = this.javaClass.getResourceAsStream(JDAConstants.CREDENTIALS_FILE_PATH)
            ?: throw FileNotFoundException("Resource not found: " + JDAConstants.CREDENTIALS_FILE_PATH)

        val credentials = JSONParser().parse(InputStreamReader(inputStream)) as JSONObject

        service = JDABuilder(credentials["token"] as String)
            .addEventListeners(CommandListener())
            .build()
    }
}