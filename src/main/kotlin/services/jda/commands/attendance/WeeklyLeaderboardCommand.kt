package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.GoogleSheets
import services.jda.commands.Command
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object WeeklyLeaderboardCommand : Command(
    parent = WeeklyCommand,
    name = "Weekly Leaderboard",
    description = "Gets the leaderboard for who has the most hours in the last week.",
    ids = listOf(
        "leaderboard",
        "lead",
        "leader",
        "l"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val weekly = AttendanceCommand.getWeekly()

        val leaderboard = AttendanceCommand.getLeaderboard(weekly)
        var first = mutableListOf<String>()
        var last = mutableListOf<String>()
        var hours = mutableListOf<String>()
        for (row in leaderboard.subList(0, 12)) {
            first.add(first.count(), row[1])
            last.add(last.count(), row[2])
            hours.add(hours.count(), row[9])
        }

        val embed = EmbedBuilder()
            .setTitle("Weekly Leaderboard")
            .setColor(ColorConstants.FALCON_MAROON)

        for (i in 0..11) {
            var hms = hours[i].split(':')
            embed.addField("**#${i + 1}:** ${first[i]} ${last[i]}", "${hms[0]}h, ${hms[1]}m, ${hms[2]}s", true)
        }

        event.channel.sendMessage(embed.build()).queue()
    }
}