package services.sheets

import services.Configuration
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Member(val discordID: String, timeRow: List<String>, logRow: List<String>, logDate: List<String>) {
    val falconTimeID = timeRow[0]
    val firstName = timeRow[1]
    val lastName = timeRow[2]
    var loggedIn = timeRow[10] == "TRUE"

    var totalPlace = 0
    var weeklyPlace = 0
    var seasonPlace = 0
    var dailyAvgPlace = 0

    val loginDate = LocalDateTime.parse(timeRow[6]).toLocalDate()

    val loginTime = Duration.ofSeconds(LocalDateTime.parse(timeRow[6]).until(LocalDateTime.now(), ChronoUnit.SECONDS))
        .let {
            if (it.toHours() < 14 && loggedIn) {
                it
            } else {
                loggedIn = false
                Duration.ZERO
            }
        }

    val lastLoginTime = timeRow[8].toDuration()

    val totalTime = timeRow[9].toDuration().plus(loginTime)

    var weeklyTime = Duration.ofSeconds(0)

    var seasonTime = Duration.ofSeconds(0)

    var dailyAverage = Duration.ofSeconds(0)

    private val logs = mutableMapOf<LocalDate, Duration>()

    init {
        for (column in 3 until logDate.count()) {
            val key = logDate[column].split(':').let { part ->
                LocalDate.of(part[0].toInt(), part[1].toInt(), part[2].toInt())
            }

            val value = if (logRow.count() > column && logRow[column] != "") {
                logRow[column].toDuration()
            } else {
                Duration.ZERO
            }

            logs[key] = value
        }

        val date = LocalDate.now()

        weeklyTime = weeklyTime.apply {
            for (i in 0..6) {
                plus(getTime(date.minusDays(i.toLong())))
            }
            plus(loginTime)
        }

        seasonTime = seasonTime.apply {
            val seasonDate = LocalDate.parse(Configuration.seasonDate)
            for (i in 0 until ChronoUnit.DAYS.between(seasonDate, date)) {
                plus(getTime(date.minusDays(i.toLong())))
            }
            plus(loginTime)
        }

        dailyAverage = logs.values.total().dividedBy(logs.count().toLong())
    }

    fun getTime(date: LocalDate) = logs[date] ?: Duration.ofSeconds(0)
}