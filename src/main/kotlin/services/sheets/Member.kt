package services.sheets

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Member(val discordID: String, timeRow: List<String>, logRow: List<String>, logDate: List<String>) {
    val falconTimeID = timeRow[0]
    val firstName = timeRow[1]
    val lastName = timeRow[2]
    val loggedIn = timeRow[10] == "TRUE"

    var totalPlace = 0
    var weeklyPlace = 0

    val loginDate = LocalDateTime.parse(timeRow[6]).toLocalDate()

    val loginTime = if (loggedIn) {
        Duration.ofSeconds(LocalDateTime.parse(timeRow[6]).until(LocalDateTime.now(), ChronoUnit.SECONDS))
    } else {
        Duration.ofSeconds(0)
    }

    val lastLoginTime = if (!loggedIn) {
        timeRow[8].split(':').let { part ->
            Duration.ofHours(part[0].toLong())
                .plusMinutes(part[1].toLong())
                .plusSeconds(part[2].toLong())
        }
    } else {
        Duration.ofSeconds(0)
    }

    val totalTime = timeRow[9].split(':').let { part ->
        Duration.ofHours(part[0].toLong())
            .plusMinutes(part[1].toLong())
            .plusSeconds(part[2].toLong())
    }.plus(
        if (loginTime.toHours() < 14) {
            loginTime
        } else {
            Duration.ofSeconds(0)
        }
    )

    var weeklyTime = Duration.ofSeconds(0)

    val logs = mutableMapOf<LocalDate, Duration>()

    init {
        for (column in 3 until logDate.count()) {
            val key = logDate[column].split(':').let { part ->
                LocalDate.of(part[0].toInt(), part[1].toInt(), part[2].toInt())
            }

            val value = if (logRow[column] != "") {
                logRow[column].split(':').let { part ->
                    Duration.ofHours(part[0].toLong())
                        .plusMinutes(part[1].toLong())
                        .plusSeconds(part[2].toLong())
                }
            } else {
                Duration.ofSeconds(0)
            }

            logs[key] = value

            val date = LocalDate.now()
            for (i in 0..6) {
                weeklyTime = weeklyTime.plus(getDateTime(date.minusDays(i.toLong())))
            }
        }
    }

    fun getDateTime(date: LocalDate) = logs[date] ?: Duration.ofSeconds(0)
}