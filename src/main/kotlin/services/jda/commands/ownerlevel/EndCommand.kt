package services.jda.commands.ownerlevel

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.json.simple.JSONArray
import org.json.simple.JSONObject
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
        "restart"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (event.author.id == "277200664424218634") {
            var json = JSONArray()
            json.add(JSONObject().put("test", "yay!"))
            var file = FileWriter("Configuration.json")
            file.write(json.toJSONString())
            file.flush()

            JDAService.service.shutdownNow()
            System.exit(0)
        }
    }

}