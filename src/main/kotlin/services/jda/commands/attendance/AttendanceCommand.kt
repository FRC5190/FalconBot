package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import services.GoogleSheets
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object AttendanceCommand : Command(
    name = "Attendance",
    description = "Gets attendance data.",
    ids = listOf(
        "attendance",
        "hrs",
        "attend",
        "hours"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val userData = GoogleSheets.service.spreadsheets().values()
            .get(SheetsConstants.falconusersSheet, "Sheet1!A2:B1000")
            .execute()

        val users = userData.getValues() as List<List<String>>
        var leaderboard = getLeaderboard()

        var userId: String = users.find { user -> user[1] == event.author.id }?.get(0) ?: ""

        if (userId != "") {
            var userRow = leaderboard.find { row -> row[0] == userId }!!
            var userPlace = leaderboard.indexOf(userRow)!! + 1
            var hms = userRow[9].split(':')
            var lastLogout = if (userRow[7] != "LOGGED IN") {
                LocalDateTime.parse(userRow[7]).format(DateTimeFormatter.ofPattern("d/M/YYYY"))
            } else {
                "Logged in"
            }

            var embed = EmbedBuilder()
                .setTitle(this.name)
                .addField("**#$userPlace:** ${userRow[1]} ${userRow[2]}", "${hms[0]}h, ${hms[1]}m, ${hms[2]}s", false)
                .addField("${lastLogout}", "", false)
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).queue()
        } else {
            event.channel.sendMessage("User not found.\n" +
                        "Use `!register` to register a new FalconTime account.\n" +
                        "Use `!register legacy [FalconTime ID]` if you already have a FalconTime account.\n" +
                        "Registration can be completed in a private dm with the bot."
            ).queue()
        }
    }

    fun getLeaderboard(): List<List<String>> {
        val data = GoogleSheets.service.spreadsheets().values()
            .get(SheetsConstants.falcontimeSheet, "Current!A2:L1000")
            .execute()

        var values = data.getValues() as MutableList<MutableList<String>>

        if (values == null || values.isEmpty()) {
            return listOf()
        } else {
            for (row in values) {
                if (row[7] != "LOGGED IN") {
                    var totalTime = row[9].split(':')

                    var lastLogin = LocalDateTime.parse(row[6])
                    var currentTime = LocalDateTime.now()

                    var hours = lastLogin.until(currentTime, ChronoUnit.HOURS)
                    currentTime.plusHours(hours)
                    var minutes = lastLogin.until(currentTime, ChronoUnit.MINUTES)
                    currentTime.plusMinutes(hours)
                    var seconds = lastLogin.until(currentTime, ChronoUnit.SECONDS)

                    row[9] = "${totalTime[0].toInt() + hours}:${totalTime[1].toInt()}:${totalTime[2].toInt()}"
                }
            }

            val comparator = Comparator<List<String>> { a, b ->
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                val hmsA = LocalDateTime.parse(a[9], formatter)
                val hmsB = LocalDateTime.parse(b[9], formatter)
                hmsA.compareTo(hmsB)
            }

            return values.sortedWith(comparator)
        }
    }

    override fun initSubcommands() {
        LeaderboardCommand.load()
    }
}