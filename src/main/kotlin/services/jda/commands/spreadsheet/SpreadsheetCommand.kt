package services.jda.commands.spreadsheet

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
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
        event.channel.sendMessage("https://docs.google.com/spreadsheets/d/1n5yxhCf6L3UeAEk7SyeoSY0Rc4Ltq63AUnVBJu5HkSw/edit#gid=1473562382").queue()
    }
}