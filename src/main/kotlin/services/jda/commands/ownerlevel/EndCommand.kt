package services.jda.commands.ownerlevel

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDAService
import services.jda.commands.Command
import services.jda.commands.CommandPermissionLevel

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
            JDAService.service.shutdownNow()
            JDAService.credentials.put("restartchannel", event.channel.id)
            System.exit(0)
        }
    }

}