package services.jda.commands.ping

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object PingCommand : Command(
    name = "Ping",
    description = "Gets the response time of the bot.",
    ids = listOf(
        "ping",
        "uptime"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        var currentTime = LocalDateTime.now()

        var days = Application.startTime.until(currentTime, ChronoUnit.DAYS)
        currentTime.minusDays(days)
        var hours = Application.startTime.until(currentTime, ChronoUnit.HOURS)
        currentTime.minusHours(hours)
        var minutes = Application.startTime.until(currentTime, ChronoUnit.MINUTES)
        currentTime.minusMinutes(minutes)
        var seconds = Application.startTime.until(currentTime, ChronoUnit.SECONDS)

        val embed = EmbedBuilder()
            .setTitle("Pong!")
            .addField("Uptime", "${days}d, ${hours}h, ${minutes}m, ${seconds}s", false)
            .addField("Gateway Latency", "${event.jda.gatewayPing}ms", false)
            .addField("REST Latency", "${event.jda.restPing.complete()}ms", false)
            .setColor(ColorConstants.FALCON_MAROON)

        event.channel.sendMessage(embed.build()).queue()
    }
}