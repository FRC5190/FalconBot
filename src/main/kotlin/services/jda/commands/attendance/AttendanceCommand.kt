package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import services.GoogleSheets
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.Duration

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
        val leaderboard = getLeaderboard()

        val userId: String = users.find { user -> user[1] == event.author.id }?.get(0) ?: ""

        if (userId != "") {
            val userRow = leaderboard.find { row -> row[0] == userId }!!
            val userPlace = leaderboard.indexOf(userRow)!! + 1
            val hms = userRow[9].split(':')
            val lastLogout = if (userRow[7] != "LOGGED IN") {
                LocalDateTime.parse(userRow[7]).format(DateTimeFormatter.ofPattern("d/M/YYYY"))
            } else {
                "Logged in"
            }

            val embed = EmbedBuilder()
                .setTitle(this.name)
                .addField("**#$userPlace:** ${userRow[1]} ${userRow[2]}", "${hms[0]}h, ${hms[1]}m, ${hms[2]}s", false)
                .addField(lastLogout, "", false)
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

        val values = data.getValues() as MutableList<MutableList<String>>

        if (values.isEmpty()) {
            return listOf()
        } else {
            for (row in values) {
                if (row[7] == "LOGGED IN" && row[9] != "") {

                    val timeParts = row[9].split(':')
                    val lastLogin = LocalDateTime.parse(row[6])
                    val currentTime = LocalDateTime.now()
                    val totalTime = Duration.ofHours(timeParts[0].toLong())

                    totalTime.plusMinutes(timeParts[1].toLong())
                    totalTime.plusSeconds(timeParts[2].toLong())

                    totalTime.plusSeconds(lastLogin.until(currentTime, ChronoUnit.SECONDS))

                    row[9] = String.format("%d:%02d:%02d", totalTime.seconds / 3600, (totalTime.seconds % 3600) / 60, (totalTime.seconds % 60));
                }
            }

            val comparator = Comparator<List<String>> { a, b ->
                val aPart = a[9].split(':')
                val bPart = b[9].split(':')

                val hmsA = if (a[9] == "") { Duration.ofSeconds(0) }
                else { {
                    val duration = Duration.ofHours(aPart[0].toLong())
                    duration.plusMinutes(aPart[1].toLong())
                    duration.plusSeconds(aPart[2].toLong())
                }.invoke() }

                val hmsB = if (b[9] == "") { Duration.ofSeconds(0) }
                else { {
                    val duration = Duration.ofHours(bPart[0].toLong())
                    duration.plusMinutes(bPart[1].toLong())
                    duration.plusSeconds(bPart[2].toLong())
                    duration
                }.invoke() }

                hmsB.compareTo(hmsA)
            }

            return values.sortedWith(comparator)
        }
    }

    override fun initSubcommands() {
        LeaderboardCommand.load()
    }
}