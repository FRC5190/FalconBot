package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command
import services.sheets.Attendance
import services.sheets.hmsTimeFormat
import services.sheets.sortDailyAvg
import services.sheets.sortWeeklyHours

object DailyLeaderboardCommand : Command(
    parent = LeaderboardCommand,
    name = "Daily Average Leaderboard",
    description = "Gets the leaderboard for who has the most average daily hours.",
    ids = listOf(
        "daily",
        "d",
        "day"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val members = Attendance.getMembers()
            .sortDailyAvg()

        val embed = EmbedBuilder()
            .setTitle("Daily Average Leaderboard")
            .setColor(ColorConstants.FALCON_MAROON)

        for (i in 0 until 12) {
            val member = members[i]
            embed.addField("**#${member.dailyAvgPlace}:** ${member.firstName} ${member.lastName}", member.dailyAverage.hmsTimeFormat(), true)
        }

        event.channel.sendMessage(embed.build()).queue()
    }
}