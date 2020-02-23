package services.sheets

import com.google.api.services.sheets.v4.model.ValueRange
import services.Configuration
import services.GoogleSheets
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Attendance {
    private var memberSheet = Sheet("falcontime", "Current!A2:L1000")
    private var logSheet = Sheet("falcontime", "Date Log!A1:ZZ1000")
    private var discordSheet = Sheet("discord", "Sheet1!A2:B1000")

    init {
        update()
    }

    fun update() {
        memberSheet.update()
        logSheet.update()
        discordSheet.update()
    }

    fun getMembers(): List<Member>{
        update()
        val members = mutableListOf<Member>()

        for (memberRow in memberSheet.values) {
            val logRow = logSheet.values.find { row ->
                row[0] == memberRow[0]
            } ?: continue

            val discord = discordSheet.values.find { row ->
                row[0] == memberRow[0]
            }?.get(1) ?: ""

            members.add(Member(discord, memberRow, logRow, logSheet.values[0]))
        }

        return members
    }

    fun getDiscordIDs(): Map<String, String> = mutableMapOf<String, String>().apply {
        discordSheet.values.forEach { row ->
            if (row.isNotEmpty()) {
                put(row[1], row[0])
            }
        }
    }

    fun setDiscordIDs(discordIDs: Map<String, String>) {
        discordSheet.values = mutableListOf<MutableList<String>>()
            .apply {
                discordIDs.forEach { id ->
                    add(mutableListOf( id.value, id.key ))
                }
            }
    }
}