package services.jda.commands.attendance

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command
import services.sheets.*
import java.time.LocalDate

object AttendanceCommand : Command(
    name = "Attendance",
    description = "Gets attendance data.",
    usage = "@member",
    ids = listOf(
        "attendance",
        "hrs",
        "attend",
        "hours"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        val members = Attendance.getMembers()
            .sortTotalHours()
            .sortWeeklyHours()
            .sortSeasonHours()
            .sortWeeklyAvg()
            .sortDailyAvg()

        val member = members.find { member ->
            member.discordID == if (args.count() < 2) {
                event.author.id
            } else {
                event.message.mentionedUsers[0].id
            }
        }

        if (member != null) {
            var embed = EmbedBuilder()
                .setTitle("Member Attendance")
                .setDescription("${member.firstName} ${member.lastName}")
                .addField("**#${member.totalPlace}** in total hours:", member.totalTime.hmsTimeFormat(), false)
                .addField("**#${member.weeklyPlace}** in the past week:", member.weeklyTime.hmsTimeFormat(), false)
                .addField("**#${member.seasonPlace}** this season:", member.seasonTime.hmsTimeFormat(), false)
                .addField("**#${member.weeklyAvgPlace}** in weekly average:", member.weeklyAverage.hmsTimeFormat(), false)
                .addField("**#${member.dailyAvgPlace}** in daily average:", member.dailyAverage.hmsTimeFormat(), false)
                .setFooter(if (member.loggedIn) {
                    "Logged in: " + (member.loginTime + member.getTime(LocalDate.now())).hmsTimeFormat()
                } else {
                    "${member.loginDate.mdyDateFormat()}: " + (member.lastLoginTime + member.getTime(LocalDate.now())).hmsTimeFormat()
                })
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            event.channel.sendMessage(embed).queue()
        } else {
            var embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("Member not found.\n" +
                        "Use `${Configuration.jdaPrefix}register` to register a new FalconTime account.\n" +
                        "Use `${Configuration.jdaPrefix}register legacy [FalconTime ID]` if you already have a FalconTime account.\n" +
                        "Registration can be completed in a private dm with the bot.")
                .setFooter("Ex: ${Configuration.jdaPrefix}register legacy 9195555555")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            event.channel.sendMessage(embed).queue()
        }
    }

    override fun initSubcommands() {
        LeaderboardCommand.load()
        BuildspaceCommand.load()
    }
}