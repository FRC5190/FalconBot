package services.jda.commands.links

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command
import services.jda.commands.CommandPermissionLevel

object RemoveLinkCommand : Command(
    permissionLevel = CommandPermissionLevel.OWNER,
    parent = LinkCommand,
    name = "Remove Link",
    description = "Removes a link displayed by the bot.",
    ids = listOf(
        "remove",
        "-"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (args.count() >= 3) {
            val links = Configuration.links.toMutableMap()
            val name = args.subList( 2, args.count() ).fold("", {acc, r -> acc + r + " "}).removeSuffix(" ")
            links.remove(name)
            Configuration.links = links
        }
    }
}