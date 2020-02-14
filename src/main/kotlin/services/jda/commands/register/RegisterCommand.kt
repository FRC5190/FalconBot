package services.jda.commands.register

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
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
            .setDescription("This command has not been implemented.\nUse `!register legacy [FalconTime ID]` to link a FalconTime account and a Discord account.")
            .setColor(ColorConstants.FALCON_MAROON)

        event.channel.sendMessage(embed.build()).queue()
    }

    override fun initSubcommands() {
        LegacyCommand.load()
    }
}