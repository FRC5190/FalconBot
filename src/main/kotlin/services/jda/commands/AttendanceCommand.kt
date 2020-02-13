package services.jda.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User
import services.GoogleSheets
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDAService
import java.awt.Color
import javax.annotation.processing.Messager

object AttendanceCommand : Command(){
    override val self = this
    override val name = "Attendance"
    override val description =
        "Gets attendance data.\n" +
        "   `!attendance `"
    override val identifiers = listOf(
        "attendance",
        "attend",
        "hours",
        "hrs"
    )

    override fun execute(event: MessageReceivedEvent) {
        val parts = event.message.contentRaw.split(' ')

        if (parts.count() > 1) {
            if (parts[1] == "lead" || parts[1] == "l" || parts[1] == "leaderboard") {
                leaderboard(event, getLeaderboard())
            } else {
                println(parts[1])
                var user = JDAService.service.getUserById(parts[1].removePrefix("<@!").removeSuffix(">"))
                if (user != null) {
                    getHours(event, getLeaderboard(), user)
                }
            }
        } else {
            getHours(event, getLeaderboard(), event.author)
        }
    }

    private fun getHours(event: MessageReceivedEvent, sorted: List<List<String>>, user: User) {
        val data = GoogleSheets.service.spreadsheets().values()
            .get(SheetsConstants.falconusersSheet, "Sheet1!A2:B1000")
            .execute()

        val falconUsers = data.getValues() as List<List<String>>

        var userNumber = ""
        for (i in 0..(falconUsers.count()-1)) {
            if (falconUsers[i][1] == user.id) {
                userNumber = falconUsers[i][0]
            }
        }

        var wantedRow = listOf<String>()
        var place = 0
        for (i in 0..(sorted.count()-1)) {
            if (sorted[i][0] == userNumber) {
                wantedRow = sorted[i]
                place = i
            }
        }

        var embed = EmbedBuilder()
            .setTitle("Attendance")
            .setDescription("#${place}: ${wantedRow[1]} ${wantedRow[2]}")
            .addField(wantedRow[9], "", false)

        event.channel.sendMessage(embed.build()).queue()
    }

    private fun leaderboard(event: MessageReceivedEvent, sorted: List<List<String>>) {

        var first = mutableListOf<String>()
        var last = mutableListOf<String>()
        var hours = mutableListOf<String>()
        for (row in sorted.subList(0, 12)) {
            first.add(first.count(), row[1])
            last.add(last.count(), row[2])
            hours.add(hours.count(), row[9])
        }

        val embed = EmbedBuilder()
            .setTitle("Attendance Leaderboard")
            .setColor(Color(204, 10, 15))

        for (i in 0..11) {
            embed.addField("#${i + 1}: ${first[i]} ${last[i]}", hours[i], true)
        }

        event.channel.sendMessage(embed.build()).queue()
    }

    private fun getLeaderboard(): List<List<String>> {
        val data = GoogleSheets.service.spreadsheets().values()
            .get(SheetsConstants.falcontimeSheet, "Current!A2:L1000")
            .execute()

        val values: List<List<String>> = data.getValues() as List<List<String>>

        if (values == null || values.isEmpty()) {
            return listOf<List<String>>()
        } else {
            val comparator = Comparator<List<String>> { a, b ->
                val hmsA = a[9].split(':')
                val hmsB = b[9].split(':')
                val aNum = try {
                    hmsA[0].toInt() * 3600 + hmsA[1].toInt() * 60 + hmsA[2].toInt()
                } catch (e: Exception) {
                    0
                }
                val bNum = try {
                    hmsB[0].toInt() * 3600 + hmsB[1].toInt() * 60 + hmsB[2].toInt()
                } catch (e: Exception) {
                    0
                }

                when {
                    aNum > bNum -> -1
                    aNum < bNum -> 1
                    else -> 0
                }
            }

            return values.sortedWith(comparator)
        }
    }
}