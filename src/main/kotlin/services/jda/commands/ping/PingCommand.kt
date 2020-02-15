package services.jda.commands.ping

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object PingCommand : Command(
    name = "Ping",
    description = "Gets the response time of the bot.",
    ids = listOf(
        "ping",
        "uptime",
        "about"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        var startTime = Application.startTime
        var currentTime = LocalDateTime.now()

        var days = startTime.until(currentTime, ChronoUnit.DAYS)
        startTime = startTime.plusDays(days)
        var hours = startTime.until(currentTime, ChronoUnit.HOURS)
        startTime = startTime.plusHours(hours)
        var minutes = startTime.until(currentTime, ChronoUnit.MINUTES)
        startTime = startTime.plusMinutes(minutes)
        var seconds = startTime.until(currentTime, ChronoUnit.SECONDS)

        val embed = EmbedBuilder()
            .setTitle("About")
            .setDescription("Type `${Configuration.jdaPrefix}help` for help.")
            .addField("Version", "${Configuration.appVersion}", true)
            .addField("Uptime", "${days}d, ${hours}h, ${minutes}m, ${seconds}s", true)
            .addField("","",true)
            .addField("Gateway Latency", "${event.jda.gatewayPing}ms", true)
            .addField("REST Latency", "${event.jda.restPing.complete()}ms", true)
            .addField("","",true)
            .setColor(ColorConstants.FALCON_MAROON)

        event.channel.sendMessage(embed.build()).queue()
    }
}