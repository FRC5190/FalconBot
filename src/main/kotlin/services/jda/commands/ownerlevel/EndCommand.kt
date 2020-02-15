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


object EndCommand : Command(
    permissionLevel = CommandPermissionLevel.OWNER,
    name = "End",
    description = "Updates bot.",
    ids = listOf(
        "end",
        "exit",
        "restart",
        "update"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (event.author.id == "277200664424218634") {
            var embed = EmbedBuilder()
                .setTitle("Restarting...")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            var restartMessage = event.channel.sendMessage(embed).complete()
            Configuration.json["restart_message"] = restartMessage.id

            if (event.channelType == ChannelType.PRIVATE) {
                Configuration.json["restart_channel"] = event.privateChannel.id
            }

            if (event.channelType == ChannelType.TEXT) {
                Configuration.json["restart_channel"] = event.textChannel.id
            }

            var file = FileWriter("configuration.json")
            file.write(Configuration.json.toJSONString())
            file.flush()

            JDAService.service.shutdownNow()
            System.exit(0)
        }
    }

}