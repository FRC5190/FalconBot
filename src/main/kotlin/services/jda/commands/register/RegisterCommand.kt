package services.jda.commands.register

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command

object RegisterCommand : Command(
    name = "Register",
    description = "Used to register a discord user as a falcon-user.",
    ids = listOf(
        "register",
        "reg"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val embed = EmbedBuilder()
            .setTitle(this.name)
            .setDescription("This command has not been implemented.\n" +
                    "Use `${Configuration.jdaPrefix}register legacy [FalconTime ID]` to link a FalconTime account and a Discord account. " +
                    "You can use the command in a private message if you don't want other members to know your ID.")
            .setFooter("Ex: `${Configuration.jdaPrefix}register legacy 9195555555`")
            .setColor(ColorConstants.FALCON_MAROON)

        event.channel.sendMessage(embed.build()).queue()
    }

    override fun initSubcommands() {
        LegacyCommand.load()
    }
}