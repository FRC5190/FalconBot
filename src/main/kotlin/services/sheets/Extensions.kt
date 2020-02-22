package services.sheets

import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

fun List<Member>.sortDailyAvg(): List<Member> {
    val sortedMembers = this
        .toMutableList().sortedBy { it.dailyAverage }
        .asReversed()

    for (position in 1 until sortedMembers.count()) {
        sortedMembers[position - 1].dailyAvgPlace = position
    }

    return sortedMembers
}

fun List<Member>.sortSeasonHours(): List<Member> {
    val sortedMembers = this
        .toMutableList().sortedBy { it.seasonTime }
        .asReversed()

    for (position in 1 until sortedMembers.count()) {
        sortedMembers[position - 1].seasonPlace = position
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
    this.format(DateTimeFormatter.ofPattern("M/d/uuuu"))

fun Collection<Duration>.total(): Duration =
    this.fold(Duration.ZERO, {acc, duration -> acc + duration })

fun String.toDuration(): Duration =
    this.split(':').let {
        Duration.ofHours(it[0].toLong())
            .plusMinutes(it[1].toLong())
            .plusSeconds(it[2].toLong())
    }