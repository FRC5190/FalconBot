package services.jda.commands.ownerlevel

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDAService
import services.jda.commands.Command
import services.jda.commands.CommandPermssionLevel

object EndCommand : Command(
    permissionLevel = CommandPermssionLevel.OWNER,
    name = "End",
    description = "Updates bot.",
    ids = listOf(
        "end"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (event.author.id == "277200664424218634") {
            JDAService.service.shutdownNow()
            System.exit(0)
        }
    }

}