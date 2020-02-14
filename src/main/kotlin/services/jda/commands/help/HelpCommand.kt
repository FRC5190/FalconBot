package services.jda.commands.help

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDAService
import services.jda.commands.Command
import services.jda.commands.CommandPermssionLevel

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
                .setTitle("**Help**")
                .setDescription("Type `!help [command]` for more information on a specific command.")
                .setColor(ColorConstants.FALCON_MAROON)

            JDAService.commands.forEach { command ->
                if (command.permissionLevel == CommandPermssionLevel.ALL) {
                    embed.addField(
                        command.name,
                        command.ids.take(2).fold("", { acc, s -> "$acc`!$s` " }).removeSuffix(" "),
                        true
                    )
                }
            }

            event.channel.sendMessage(embed.build()).queue()
        } else {
            var stringArgs = args.subList(1, args.count()).fold("", {acc, s -> "$acc$s "}).removeSuffix(" ").toLowerCase()
            if (JDAService.commandIds.containsKey(stringArgs) && JDAService.commandIds[stringArgs]!!.permissionLevel == CommandPermssionLevel.ALL) {
                val command = JDAService.commandIds[stringArgs]!!

                val embed = EmbedBuilder()
                    .setTitle("**${command.name}**")
                    .setDescription(command.description)
                    .setColor(ColorConstants.FALCON_MAROON)
                    .addField(
                        command.ids.fold("", {acc, s -> "$acc`!$s` "}).removeSuffix(" "),
                        "**~~---------------------------------------------------~~**\n**__Subcommands:__**",
                        false
                    )

                command.children.forEach { child ->
                    embed.addField(
                        "${child.name}",
                        child.ids.take(2).fold("", {acc, s -> "$acc`...$s` "}).removeSuffix(" "),
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