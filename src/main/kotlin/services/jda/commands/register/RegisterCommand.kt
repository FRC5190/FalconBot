package services.jda.commands.register

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command
import services.jda.sessions.register.RegisterSession
import services.sheets.Attendance

object RegisterCommand : Command(
    name = "Register",
    description = "Used to register a discord user as a falcon-user.",
    ids = listOf(
        "register",
        "reg"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (Attendance.getDiscordIDs().containsKey(event.author.id)) {
            val embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("You are already registered.")
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).queue()
        } else {
            RegisterSession().apply {
                attach(event.author)
                invoke(event, args)
            }
        }
    }

    override fun initSubcommands() {
        LegacyCommand.load()
    }
}