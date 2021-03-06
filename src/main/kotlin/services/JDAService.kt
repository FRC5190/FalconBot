package services

import net.dv8tion.jda.api.JDA
import services.jda.commands.Command
import net.dv8tion.jda.api.JDABuilder
import org.json.simple.JSONObject
import org.slf4j.LoggerFactory
import services.jda.CommandListener
import services.jda.commands.attendance.AttendanceCommand
import services.jda.commands.countdown.CountdownCommand
import services.jda.commands.help.HelpCommand
import services.jda.commands.ownerlevel.ChannelCommand
import services.jda.commands.ownerlevel.RestartCommand
import services.jda.commands.ping.PingCommand
import services.jda.commands.register.RegisterCommand
import services.jda.commands.links.LinkCommand
import services.jda.commands.ownerlevel.VersionCommand

object JDAService {
    val logger = LoggerFactory.getLogger("Application")

    lateinit var service: JDA
    lateinit var credentials: JSONObject
    var commandIds = mutableMapOf<String, Command>()
    var commands = mutableListOf<Command>()

    fun start() {
        logger.info("Starting JDA...")

        service = JDABuilder(Configuration.jdaToken)
            .addEventListeners(CommandListener())
            .build()
            .awaitReady()

        loadCommands()
        logger.info("Initialized JDA")
    }

    private fun loadCommands() {
        AttendanceCommand.load()
        LinkCommand.load()
        HelpCommand.load()
        RegisterCommand.load()
        PingCommand.load()
        RestartCommand.load()
        ChannelCommand.load()
        CountdownCommand.load()
        VersionCommand.load()
    }
}