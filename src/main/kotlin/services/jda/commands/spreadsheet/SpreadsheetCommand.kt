package services.jda.commands.spreadsheet

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.Configuration
import services.jda.commands.Command

object SpreadsheetCommand: Command(
    name = "Spreadsheet",
    description = "Retrieves the link for the attendance spreadsheet.",
    ids = listOf(
        "spreadsheet",
        "sheet"
    )
) {
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        event.channel.sendMessage(Configuration.links["attendance"] ?: "Could not retrieve spreadsheet link at this time.").queue()
    }
}