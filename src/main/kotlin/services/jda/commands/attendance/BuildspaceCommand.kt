package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.GoogleSheets
import services.jda.commands.Command
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object BuildspaceCommand : Command(
    parent = AttendanceCommand,
    name = "Buildspace",
    description = "Returns a list of all users that are at the buildspace.",
    ids = listOf(
        "buildspace",
        "b",
        "build"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val timeData = GoogleSheets.service.spreadsheets().values()
            .get(Configuration.sheets["times"], "Current!A2:L1000")
            .execute()

        val timeValues = timeData.getValues() as MutableList<MutableList<String>>
        val currentTime = LocalDateTime.now()

        val embed = EmbedBuilder()
            .setTitle("Members at buildspace")
            .setColor(ColorConstants.FALCON_MAROON)

        var fields = 0
        timeValues.filter { it[10] == "TRUE" }.forEach { timeRow ->
            val lastLogin = LocalDateTime.parse(timeRow[6])
            val loginTime = Duration.ofSeconds(lastLogin.until(currentTime, ChronoUnit.SECONDS))
            if (loginTime.toHours() < 14) {
                fields++
                embed.addField(
                    "${timeRow[1]} ${timeRow[2]}",
                    String.format(
                        "%dh, %02dm, %02ds",
                        loginTime.seconds / 3600,
                        (loginTime.seconds % 3600) / 60,
                        (loginTime.seconds % 60)
                    ),
                    true
                )
            }
        }

        if (fields == 0) {
            embed.setDescription("There are no members at the buildspace.")
        }

        event.channel.sendMessage(embed.build()).queue()
    }
}