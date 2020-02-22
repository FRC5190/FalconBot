package services.jda.commands.links

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command
import services.jda.commands.CommandPermissionLevel
import services.jda.commands.countdown.CountdownCommand

object RemoveCountCommand : Command(
    permissionLevel = CommandPermissionLevel.OWNER,
    parent = CountdownCommand,
    name = "Remove Countdown",
    description = "Removes a countdown displayed by the bot.",
    ids = listOf(
        "remove",
        "-",
        "rm"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (args.count() >= 3) {
            val countdowns = Configuration.countdowns.toMutableMap()
            val name = args.subList( 2, args.count() ).fold("", {acc, r -> acc + r + " "}).removeSuffix(" ")
            countdowns.remove(name)
            Configuration.countdowns = countdowns
        }
    }
}