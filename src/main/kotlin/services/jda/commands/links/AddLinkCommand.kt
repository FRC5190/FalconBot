package services.jda.commands.links

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command
import services.jda.commands.CommandPermissionLevel

object AddLinkCommand : Command(
    permissionLevel = CommandPermissionLevel.OWNER,
    parent = LinkCommand,
    name = "Add Link",
    description = "Adds a link to be displayed by the bot.",
    ids = listOf(
        "add",
        "+"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (args.count() >= 4) {
            val links = Configuration.links.toMutableMap()
            val name = args.subList( 2, args.count() - 1 ).fold("", {acc, r -> acc + r + " "}).removeSuffix(" ")
            links[name] = args.last()
            Configuration.links = links
        }
    }
}