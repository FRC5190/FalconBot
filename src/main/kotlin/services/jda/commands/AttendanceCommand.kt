package services.jda.commands

import services.GoogleSheets
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object AttendanceCommand : Command(){
    override val self = this
    override val identifiers = listOf(
        "attendence",
        "attend",
        "hours",
        "hrs"
    )

    override fun execute(event: MessageReceivedEvent) {
        val data = GoogleSheets.service.spreadsheets().values()
            .get(SheetsConstants.falcontimeSheet, "Current!A2:L1000")
            .execute()

        val values: List<List<String>> = data.getValues() as List<List<String>>

        if (values == null || values.isEmpty()) {
            println("No data found.")
            event.channel.sendMessage("No data found")
        } else {
            val comparator = Comparator<List<String>> { a, b ->
                val hmsA = a[9].split(':')
                val hmsB = b[9].split(':')
                val aNum = try {
                    hmsA[0].toInt() * 3600 + hmsA[1].toInt() * 60 + hmsA[2].toInt()
                } catch(e: Exception) { 0 }
                val bNum = try {
                    hmsB[0].toInt() * 3600 + hmsB[1].toInt() * 60 + hmsB[2].toInt()
                } catch(e: Exception) { 0 }

                when {
                    aNum > bNum -> -1
                    aNum < bNum -> 1
                    else -> 0
                }
            }

            val sorted = values.sortedWith(comparator)
            var parsed = ""
            for (row in sorted.subList(0, 10)) {
                parsed += "${row[1]}, ${row[9]} \n"
            }

            event.channel.sendMessage(parsed).queue()
        }
    }
}