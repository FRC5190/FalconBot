package services

import JDAConstants
import net.dv8tion.jda.api.JDA
import services.jda.commands.AttendanceCommand
import services.jda.commands.Command
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import services.jda.CommandListener
import java.io.FileNotFoundException
import java.io.InputStreamReader

object JDA {
    lateinit var service: JDA
    var commands = mutableMapOf<String, Command>()

    fun start() {
        val inputStream = this.javaClass.getResourceAsStream(JDAConstants.CREDENTIALS_FILE_PATH)
            ?: throw FileNotFoundException("Resource not found: " + JDAConstants.CREDENTIALS_FILE_PATH)

        val credentials = JSONParser().parse(InputStreamReader(inputStream)) as JSONObject

        println("Starting JDA")

        service = JDABuilder("Njc2NTc1NzAzODUxOTkxMDU0.XkRvpw.9R-BvsUMVnPLSa_pB3X-uOwZFRs")
            .addEventListeners(CommandListener())
            .build()

        println("test")
    }
}