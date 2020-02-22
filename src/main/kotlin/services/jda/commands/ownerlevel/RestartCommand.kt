package services.jda.commands.ownerlevel

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import services.Configuration
import services.JDAService
import services.jda.commands.Command
import services.jda.commands.CommandPermissionLevel
import java.io.FileWriter


object RestartCommand : Command(
    permissionLevel = CommandPermissionLevel.OWNER,
    name = "Restart",
    description = "Updates bot.",
    ids = listOf(
        "end",
        "exit",
        "restart",
        "update"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        JDAService.service.shutdownNow()
        System.exit(0)
    }
}