package services.jda.commands.register

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.GoogleSheets
import services.jda.commands.Command
import services.sheets.Attendance

object LegacyCommand : Command(
    parent = RegisterCommand,
    name = "Register Legacy",
    description = "Links a FalconTime account with a Discord account.",
    usage = "9195555555",
    ids = listOf(
        "legacy"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        var discordIDs = Attendance.getDiscordIDs().toMutableMap()

        if (args.count() <= 2) {
            var embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("Invalid FalconTime ID specified.")
                .setFooter("Example: ${Configuration.jdaPrefix}register legacy 9195555555")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            event.channel.sendMessage(embed).queue()
        } else if (discordIDs.containsKey(event.author.id)) {
            var embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("Your Discord account is already linked with FalconTime ID ${discordIDs[event.author.id]}.")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            event.channel.sendMessage(embed).queue()
        } else if (discordIDs.containsValue(args[3])) {
            var embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("FalconTime ID ${args[3]} has already been linked to another Discord account.")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            event.channel.sendMessage(embed).queue()
        } else if (!Attendance.getMembers().any { it.falconTimeID == args[3] }) {
            var embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("Invalid FalconTime ID specified.")
                .setFooter("Example: ${Configuration.jdaPrefix}register legacy 9195555555")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            event.channel.sendMessage(embed).queue()
        } else {
            discordIDs[event.author.id] = args[3]
            Attendance.setDiscordIDs(discordIDs)

            var embed = EmbedBuilder()
                .setTitle("FalconTime and Discord account linked!")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            event.channel.sendMessage(embed).queue()
        }
    }
}