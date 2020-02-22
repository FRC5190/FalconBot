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

        Configuration.countdowns.forEach { (name, datetime) ->
            var startTime = LocalDateTime.parse(datetime)
            var currentTime = LocalDateTime.now()

            if (currentTime.isAfter(startTime)) {
                Configuration.countdowns = Configuration.countdowns.toMutableMap().apply { remove(name) }
            } else {
                var days = currentTime.until(startTime, ChronoUnit.DAYS)
                currentTime = currentTime.plusDays(days)
                var hours = currentTime.until(startTime, ChronoUnit.HOURS)
                currentTime = currentTime.plusHours(hours)
                var minutes = currentTime.until(startTime, ChronoUnit.MINUTES)
                currentTime = currentTime.plusMinutes(minutes)
                var seconds = currentTime.until(startTime, ChronoUnit.SECONDS)
                currentTime = currentTime.plusSeconds(seconds)

                val countdown = "${days}d, ${hours}h, ${minutes}m, ${seconds}s"
                val event = name.split(' ').fold("", {acc, r -> acc + r.capitalize() + " "}).removeSuffix(" ")
                val date = datetime.format(DateTimeFormatter.ofPattern("M/d/uu"))

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