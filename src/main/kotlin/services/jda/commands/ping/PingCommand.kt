package services.jda.commands.ping

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command

object PingCommand : Command(
    name = "Ping",
    description = "Gets the response time of the bot.",
    ids = listOf(
        "ping"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val embed = EmbedBuilder()
            .setTitle("Pong!")
            .addField("Gateway Latency", "${event.jda.gatewayPing}ms", false)
            .addField("REST Latency", "${event.jda.restPing.complete()}ms", false)
            .setColor(ColorConstants.FALCON_MAROON)

        event.channel.sendMessage(embed.build()).queue()
    }
}