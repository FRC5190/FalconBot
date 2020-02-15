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
            var temp = JSONObject()

            if (event.channelType == ChannelType.PRIVATE) {
                temp["restart_channel"] = event.author.id
                var file = FileWriter("temp.json")
                file.write(temp.toJSONString())
                file.flush()
            }else if (event.channelType == ChannelType.TEXT) {
                temp["restart_channel"] = event.textChannel.id
                var file = FileWriter("temp.json")
                file.write(temp.toJSONString())
                file.flush()
            }

            JDAService.service.shutdownNow()
            System.exit(0)
        }
    }

}