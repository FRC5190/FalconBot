package services.jda.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDAService

object HelpCommand: Command() {
    override val self = this
    override val name = "Help"
    override val description = "Nice."
    override val identifiers = listOf<String>(
        "help"
    )

    override fun execute(event: MessageReceivedEvent) {
        val parts = event.message.contentRaw.toLowerCase().split(' ')
        if (parts.size < 2) {
            val embed = EmbedBuilder()
                .setTitle("Help")
                .setDescription("Type `!help [command]` for more information on a specific command.")

            for (command in JDAService.commands) {
                embed.addField(command.name, command.identifiers.take(2).fold("", {acc, s -> "$acc`!$s` "}).removeSuffix(" "), true)
            }

            event.channel.sendMessage(embed.build()).queue()
        } else {
            if (JDAService.commandIdentifiers.containsKey(parts[1])) {
                val command = JDAService.commandIdentifiers[parts[1]]!!

                val embed = EmbedBuilder()
                    .setTitle("Help")
                    .setDescription("**${command.name}**\n${command.description}")
                    .addField(command.identifiers.fold("", {acc, s -> "$acc`!$s` "}).removeSuffix(" "), "", false)

                event.channel.sendMessage(embed.build()).queue()
            } else {
                event.channel.sendMessage("Unrecognized command `${parts[1]}`")
            }
        }
    }
}