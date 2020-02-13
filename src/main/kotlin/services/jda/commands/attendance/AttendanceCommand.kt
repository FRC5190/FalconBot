package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User
import services.GoogleSheets
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDAService
import services.jda.commands.Command
import java.awt.Color

object AttendanceCommand : Command(
    name = "Attendance",
    description = "Gets attendance data.",
    ids = listOf(
        "attendance",
        "attend",
        "hours",
        "hrs"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val userData = GoogleSheets.service.spreadsheets().values()
            .get(SheetsConstants.falconusersSheet, "Sheet1!A2:B1000")
            .execute()

        val users = userData.getValues() as List<List<String>>
        var leaderboard = getLeaderboard()

        try {
            var userId: String = users.find { user -> user[1] == event.author.id }!!.get(0)
            var userRow = leaderboard.find { row -> row[0] == userId }!!
            var userPlace = leaderboard.indexOf(userRow)!!

            var embed = EmbedBuilder()
                .setTitle(this.name)
                .setDescription("#$userPlace: ${userRow[1]} ${userRow[2]}")
                .addField("${userRow[9]}", "", false)
                .setColor(Color(104, 10, 15))

            event.channel.sendMessage(embed.build()).queue()
        } catch(e: Exception) {
            event.channel.sendMessage("User not found.\nUse `!register` to register your account.").queue()
        }
    }

    fun getLeaderboard(): List<List<String>> {
        val data = GoogleSheets.service.spreadsheets().values()
            .get(SheetsConstants.falcontimeSheet, "Current!A2:L1000")
            .execute()

        val values: List<List<String>> = data.getValues() as List<List<String>>

        if (values == null || values.isEmpty()) {
            return listOf()
        } else {
            val comparator = Comparator<List<String>> { a, b ->
                val hmsA = a[9].split(':')
                val hmsB = b[9].split(':')
                val aNum = try {
                    hmsA[0].toInt() * 3600 + hmsA[1].toInt() * 60 + hmsA[2].toInt()
                } catch (e: Exception) { 0 }
                val bNum = try {
                    hmsB[0].toInt() * 3600 + hmsB[1].toInt() * 60 + hmsB[2].toInt()
                } catch (e: Exception) { 0 }

                when {
                    aNum > bNum -> -1
                    aNum < bNum -> 1
                    else -> 0
                }
            }

            return values.sortedWith(comparator)
        }
    }

    override fun initSubcommands() {
        LeaderboardCommand.load()
    }
}