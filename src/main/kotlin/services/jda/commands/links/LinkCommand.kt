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

        val links = mutableListOf<Pair<String, String>>().apply {
            Configuration.links.forEach { (name, link) ->
                add(Pair(name, link))
            }
        }

        for (link in 0 until links.count() step 2) {
            val link1 = links.getOrNull(link)?.let { "[${it.first}](${it.second})" } ?: ""
            val link2 = links.getOrNull(link + 1)?.let { "[${it.first}](${it.second})" } ?: ""
            embed.addField(link1, link2, true)
        }

        event.channel.sendMessage(embed.build()).queue()
    }

    override fun initSubcommands() {
        AddLinkCommand.load()
        RemoveLinkCommand.load()
    }
}