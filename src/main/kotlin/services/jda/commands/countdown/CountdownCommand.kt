package services.jda.commands.countdown

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command
import services.jda.commands.links.AddCountCommand
import services.jda.commands.links.RemoveCountCommand
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object CountdownCommand: Command(
    name = "Countdown",
    description = "Retrieves useful countdowns.",
    ids = listOf(
        "countdown",
        "count"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val embed = EmbedBuilder()
            .setTitle("Countdowns")
            .setColor(ColorConstants.FALCON_MAROON)

        Configuration.countdowns.forEach { name, datetime ->
            var startTime = LocalDateTime.parse(datetime)
            var currentTime = LocalDateTime.now()

            embed.addField(startTime.toString(), "", true)

            if (currentTime.isAfter(startTime)) {
                Configuration.countdowns = Configuration.countdowns.toMutableMap().apply { remove(name) }
            } else {
                var days = startTime.until(currentTime, ChronoUnit.DAYS)
                startTime = startTime.plusDays(days)
                var hours = startTime.until(currentTime, ChronoUnit.HOURS)
                startTime = startTime.plusHours(hours)
                var minutes = startTime.until(currentTime, ChronoUnit.MINUTES)
                startTime = startTime.plusMinutes(minutes)
                var seconds = startTime.until(currentTime, ChronoUnit.SECONDS)

                val countdown = "${days}d, ${hours}h, ${minutes}m, ${seconds}s"
                val event = name.split(' ').fold("", {acc, r -> acc + r.capitalize() + " "})
                val date = datetime.format(DateTimeFormatter.ofPattern("M/d/Y ha"))

                embed.addField("**$event:** $date", countdown, true)
            }
        }

        event.channel.sendMessage(embed.build()).queue()
    }

    override fun initSubcommands() {
        AddCountCommand.load()
        RemoveCountCommand.load()
    }
}