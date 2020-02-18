package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command
import services.sheets.Attendance
import services.sheets.Attendance.hmsTimeFormat
import services.sheets.Attendance.sortSeasonHours

object SeasonLeaderboardCommand : Command(
    parent = LeaderboardCommand,
    name = "Season Leaderboard",
    description = "Gets the leaderboard for most hours since buildseason.",
    ids = listOf(
        "season",
        "s"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val members = Attendance.getMembers()
            .sortSeasonHours()

        val embed = EmbedBuilder()
            .setTitle("Season Hour Leaderboard")
            .setColor(ColorConstants.FALCON_MAROON)

        for (i in 0 until 12) {
            val member = members[i]
            embed.addField("**#${member.seasonPlace}:** ${member.firstName} ${member.lastName}", member.seasonTime.hmsTimeFormat(), true)
        }

        event.channel.sendMessage(embed.build()).queue()
    }
}