package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command
import services.sheets.Attendance
import services.sheets.Attendance.hmsTimeFormat
import services.sheets.Attendance.sortWeeklyHours

object WeeklyLeaderboardCommand : Command(
    parent = LeaderboardCommand,
    name = "Weekly Leaderboard",
    description = "Gets the leaderboard for who has the most hours in the last week.",
    ids = listOf(
        "weekly",
        "w",
        "week"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val members = Attendance.getMembers()
            .sortWeeklyHours()

        val embed = EmbedBuilder()
            .setTitle("Weekly Hours Leaderboard")
            .setColor(ColorConstants.FALCON_MAROON)

        for (i in 0 until 12) {
            val member = members[i]
            embed.addField("**#${member.weeklyPlace}:** ${member.firstName} ${member.lastName}", member.weeklyTime.hmsTimeFormat(), true)
        }

        event.channel.sendMessage(embed.build()).queue()
    }
}