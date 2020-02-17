package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.GoogleSheets
import services.jda.commands.Command
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object WeeklyCommand : Command(
    parent = AttendanceCommand,
    name = "Weekly Attendance",
    description = "Gets weekly attendance data.",
    ids = listOf(
        "weekly",
        "w",
        "week"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val userData = GoogleSheets.service.spreadsheets().values()
            .get(Configuration.sheets["users"], "Sheet1!A2:B1000")
            .execute()

        val users = userData.getValues() as List<List<String>>
        val leaderboard = AttendanceCommand.getLeaderboard(AttendanceCommand.getWeekly())

        val userId: String = users.find { user -> user[1] == event.author.id }?.get(0) ?: ""

        if (userId != "") {
            val userRow = leaderboard.find { row -> row[0] == userId }!!
            val userPlace = leaderboard.indexOf(userRow)!! + 1
            val hms = userRow[9].split(':').let { "${it[0]}h, ${it[1]}m, ${it[2]}s" }
            val lastLogout = if (!userRow[7].contains("LOGGED IN ")) {
                LocalDateTime.parse(userRow[7]).format(DateTimeFormatter.ofPattern("M/d/YYYY")) as String +
                        " for " + userRow[8].split(':').let { "${it[0]}h, ${it[1]}m, ${it[2]}s" }
            } else {
                "Logged in for " + userRow[7].removePrefix("LOGGED IN ")
            }

            val embed = EmbedBuilder()
                .setTitle(this.name)
                .addField("**#$userPlace:** ${userRow[1]} ${userRow[2]}", hms, false)
                .addField(lastLogout, "", false)
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).queue()
        } else {
            var embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("User not found.\n" +
                        "Use `${Configuration.jdaPrefix}register` to register a new FalconTime account.\n" +
                        "Use `${Configuration.jdaPrefix}register legacy [FalconTime ID]` if you already have a FalconTime account.\n" +
                        "Registration can be completed in a private dm with the bot.")
                .setFooter("Ex: ${Configuration.jdaPrefix}register legacy 9195555555")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            event.channel.sendMessage(embed).queue()
        }
    }

    override fun initSubcommands() {
        WeeklyLeaderboardCommand.load()
        WeeklyMemberCommand.load()
    }
}