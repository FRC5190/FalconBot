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
        var timeSince = Application.getTime() - Application.startTime
        var timeDays = timeSince / 86400000
        var timeHours = (timeSince % 86400000) / 3600000
        var timeMins = ((timeSince % 86400000) % 3600000) / 60000
        var timeSecs = (((timeSince % 86400000) % 3600000) % 60000) / 1000

        val embed = EmbedBuilder()
            .setTitle("Pong!")
            .addField("Uptime", "$timeDays days, $timeHours hours, $timeMins minutes, $timeSecs seconds", false)
            .addField("Gateway Latency", "${event.jda.gatewayPing}ms", false)
            .addField("REST Latency", "${event.jda.restPing.complete()}ms", false)
            .setColor(ColorConstants.FALCON_MAROON)

        event.channel.sendMessage(embed.build()).queue()
    }
}