package services.jda.commands.ownerlevel

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.JDAService
import services.jda.commands.Command
import services.jda.commands.CommandPermissionLevel

object VersionCommand : Command(
    permissionLevel = CommandPermissionLevel.OWNER,
    name = "Version",
    description = "Sets bot version.",
    ids = listOf(
        "version"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        Configuration.appVersion = args[1]
    }
}