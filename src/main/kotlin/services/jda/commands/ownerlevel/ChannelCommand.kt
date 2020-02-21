package services.jda.commands.ownerlevel

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.JDAService
import services.jda.commands.Command
import services.jda.commands.CommandPermissionLevel

object ChannelCommand : Command(
    permissionLevel = CommandPermissionLevel.OWNER,
    name = "Channel",
    description = "Sets bot channel.",
    ids = listOf(
        "channel"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (Configuration.owners.any { it == event.author.id }) {
            Configuration.botChannel = event.channel.id
        }
    }
}