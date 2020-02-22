package services.jda.commands.links

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command
import services.jda.commands.CommandPermissionLevel
import services.jda.commands.countdown.CountdownCommand

object AddCountCommand : Command(
    permissionLevel = CommandPermissionLevel.OWNER,
    parent = CountdownCommand,
    name = "Add Countdown",
    description = "Adds a countdown to be displayed by the bot.",
    ids = listOf(
        "add",
        "+"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (args.count() >= 4) {
            val countdowns = Configuration.countdowns.toMutableMap()
            val name = args.subList( 2, args.count() - 1 ).fold("", {acc, r -> acc + r + " "}).removeSuffix(" ")
            countdowns[name] = args.last()
            Configuration.countdowns = countdowns
        }
    }
}