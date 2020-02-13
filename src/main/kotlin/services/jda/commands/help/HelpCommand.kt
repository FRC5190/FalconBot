package services.jda.commands.help

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDAService
import services.jda.commands.Command
import java.awt.Color

object HelpCommand: Command(
    name = "Help",
    description = "Nice.",
    ids = listOf(
        "help"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {

        if (args.size == 1) {
            val embed = EmbedBuilder()
                .setTitle("Help")
                .setDescription("Type `!help [command]` for more information on a specific command.")
                .setColor(Color(104, 10, 15))

            JDAService.commands.forEach { command ->
                embed.addField(
                    command.name,
                    command.ids.take(2).fold("", {acc, s -> "$acc`!$s` "}).removeSuffix(" "),
                    true
                )
            }

            event.channel.sendMessage(embed.build()).queue()
        } else {
            var stringArgs = args.subList(1, args.count()).fold("", {acc, s -> "$acc$s "}).removeSuffix(" ").toLowerCase()
            if (JDAService.commandIds.containsKey(stringArgs)) {
                val command = JDAService.commandIds[stringArgs]!!

                val embed = EmbedBuilder()
                    .setTitle("Help")
                    .setDescription("**${command.name}**\n${command.description}")
                    .setColor(Color(104, 10, 15))
                    .addField(
                        command.ids.fold("", {acc, s -> "$acc`!$s` "}).removeSuffix(" "),
                        "~~- - - - - - - - - - - - - - - - - - - - - - - - -~~",
                        false
                    )

                command.children.forEach { child ->
                    embed.addField(
                        "${child.name}",
                        child.ids.take(2).fold("", {acc, s -> "$acc`!${command.ids[0]} $s` "}).removeSuffix(" "),
                        true
                    )
                }

                event.channel.sendMessage(embed.build()).queue()
            } else {
                event.channel.sendMessage("Unrecognized command `!${stringArgs}`")
            }
        }
    }
}