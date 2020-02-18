package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.jda.commands.Command
import services.sheets.Attendance
import services.sheets.Attendance.hmsTimeFormat
import java.time.LocalDate

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
        val loggedInMembers = Attendance.getMembers()
            .filter { it.loggedIn }
            .sortedBy { (it.loginTime + it.getDateTime(LocalDate.now())) }
            .asReversed()

        val embed = EmbedBuilder()
            .setTitle("Members at Buildspace")
            .setColor(ColorConstants.FALCON_MAROON)

        if (loggedInMembers.count() != 0) {
            for (member in loggedInMembers) {
                embed.addField(
                    "${member.firstName} ${member.lastName}",
                    (member.loginTime + member.getDateTime(LocalDate.now())).hmsTimeFormat(),
                    true
                )
            }
        } else {
            embed.setDescription("There are no members at the buildspace.")
        }

        event.channel.sendMessage(embed.build()).queue()
}
}