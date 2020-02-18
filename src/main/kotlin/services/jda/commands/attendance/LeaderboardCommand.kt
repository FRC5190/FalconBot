package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command
import services.sheets.Attendance
import services.sheets.Attendance.sortTotalHours
import services.sheets.Attendance.hmsTimeFormat

object LeaderboardCommand : Command(
    parent = AttendanceCommand,
    name = "Attendance Leaderboard",
    description = "Gets the attendance leaderboard",
    ids = listOf(
        "leaderboard",
        "l",
        "leader",
        "lead"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val members = Attendance.getMembers()
            .sortTotalHours()

        val embed = EmbedBuilder()
            .setTitle("Total Hour Leaderboard")
            .setColor(ColorConstants.FALCON_MAROON)

        for (i in 0 until 12) {
            val member = members[i]
            embed.addField("**#${member.totalPlace}:** ${member.firstName} ${member.lastName}", member.totalTime.hmsTimeFormat(), true)
        }

        event.channel.sendMessage(embed.build()).queue()
    }

    override fun initSubcommands() {
        WeeklyLeaderboardCommand.load()
    }
}