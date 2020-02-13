package services

import JDAConstants
import net.dv8tion.jda.api.JDA
import services.jda.commands.Command
import net.dv8tion.jda.api.JDABuilder
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.slf4j.LoggerFactory
import services.jda.CommandListener
import services.jda.commands.AttendanceCommand
import services.jda.commands.HelpCommand
import services.jda.commands.SpreadsheetCommand
import java.io.FileNotFoundException
import java.io.InputStreamReader

object JDAService {
    val logger = LoggerFactory.getLogger("Application")

    lateinit var service: JDA
    var commandIdentifiers = mutableMapOf<String, Command>()
    var commands = mutableListOf<Command>()

    fun start() {
        logger.info("Starting JDA...")

        val inputStream = this.javaClass.getResourceAsStream(JDAConstants.CREDENTIALS_FILE_PATH)
            ?: throw FileNotFoundException("Resource not found: " + JDAConstants.CREDENTIALS_FILE_PATH)

        val credentials = JSONParser().parse(InputStreamReader(inputStream)) as JSONObject

        service = JDABuilder(credentials["token"] as String)
            .addEventListeners(CommandListener())
            .build()
            .awaitReady()

        loadCommands()
        logger.info("Initialized JDA")
    }

    private fun loadCommands() {
        AttendanceCommand.load()
        SpreadsheetCommand.load()
        HelpCommand.load()
    }
}