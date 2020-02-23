package services.jda.commands.links

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command

object LinkCommand: Command(
    name = "Links",
    description = "Retrieves useful team links.",
    ids = listOf(
        "links",
        "link"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val embed = EmbedBuilder()
            .setTitle("Links")
            .setColor(ColorConstants.FALCON_MAROON)

        Configuration.links.forEach { name, link ->
            embed.addField(name.split(' ').fold("", {acc, r -> acc + r.capitalize() + " "}), "[link]($link)", true)
        }

        event.channel.sendMessage(embed.build()).queue()
    }

    override fun initSubcommands() {
        AddLinkCommand.load()
        RemoveLinkCommand.load()
    }
}