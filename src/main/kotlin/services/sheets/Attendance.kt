package services.sheets

import services.Configuration
import services.GoogleSheets
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Attendance {
    private var memberSheet = GoogleSheets.service.spreadsheets().values()
        .get(Configuration.sheets["falcontime"], "Current!A2:L1000")
        .execute().getValues() as MutableList<MutableList<String>>

    private var logSheet = GoogleSheets.service.spreadsheets().values()
        .get(Configuration.sheets["falcontime"], "Date Log!A1:ZZ1000")
        .execute().getValues() as MutableList<MutableList<String>>

    private var discordSheet = GoogleSheets.service.spreadsheets().values()
        .get(Configuration.sheets["discord"], "Sheet1!A2:B1000")
        .execute().getValues() as MutableList<MutableList<String>>

    fun update() {
        memberSheet = GoogleSheets.service.spreadsheets().values()
            .get(Configuration.sheets["falcontime"], "Current!A2:L1000")
            .execute().getValues() as MutableList<MutableList<String>>
        logSheet = GoogleSheets.service.spreadsheets().values()
            .get(Configuration.sheets["falcontime"], "Date Log!A1:ZZ1000")
            .execute().getValues() as MutableList<MutableList<String>>
        discordSheet = GoogleSheets.service.spreadsheets().values()
            .get(Configuration.sheets["discord"], "Sheet1!A2:B1000")
            .execute().getValues() as MutableList<MutableList<String>>
    }

    fun getMembers(): List<Member>{
        val members = mutableListOf<Member>()

        for (memberRow in memberSheet) {
            val logRow = logSheet.find { row ->
                row[0] == memberRow[0]
            } ?: continue

            val discord = discordSheet.find { row ->
                row[0] == memberRow[0]
            }?.get(1) ?: ""

            members.add(Member(discord, memberRow, logRow, logSheet[0]))
        }

        return members
    }

    fun List<Member>.sortTotalHours(): List<Member> {
        val sortedMembers = this
            .toMutableList().sortedBy { it.totalTime }
            .asReversed()

        for (position in 1 until sortedMembers.count()) {
            sortedMembers[position - 1].totalPlace = position
        }

        return sortedMembers
    }

    fun List<Member>.sortWeeklyHours(): List<Member> {
        val sortedMembers = this
            .toMutableList().sortedBy { it.weeklyTime }
            .asReversed()

        for (position in 1 until sortedMembers.count()) {
            sortedMembers[position - 1].weeklyPlace = position
        }

        return sortedMembers
    }

    fun Duration.hmsTimeFormat(): String =
        String.format(
            "%dh, %02dm, %02ds",
            this.seconds / 3600,
            (this.seconds % 3600) / 60,
            this.seconds % 60
        )

    fun LocalDate.mdyDateFormat(): String =
        this.format(DateTimeFormatter.ofPattern("M/d/YYYY"))
}