package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command

object LeaderboardCommand : Command(
    parent = AttendanceCommand,
    name = "Leaderboard",
    description = "Gets the attendance leaderboard",
    ids = listOf(
        "leaderboard",
        "lead",
        "leader",
        "l"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        var leaderboard = AttendanceCommand.getLeaderboard()
        var first = mutableListOf<String>()
        var last = mutableListOf<String>()
        var hours = mutableListOf<String>()
        for (row in leaderboard.subList(0, 12)) {
            first.add(first.count(), row[1])
            last.add(last.count(), row[2])
            hours.add(hours.count(), row[9])
        }

        val embed = EmbedBuilder()
            .setTitle("Attendance Leaderboard")
            .setColor(ColorConstants.FALCON_MAROON)

        for (i in 0..11) {
            var hms = hours[i].split(':')
            embed.addField("**#${i + 1}:** ${first[i]} ${last[i]}", "${hms[0]} hrs, ${hms[1]} min, ${hms[2]} sec", true)
        }

        event.channel.sendMessage(embed.build()).queue()
    }
}