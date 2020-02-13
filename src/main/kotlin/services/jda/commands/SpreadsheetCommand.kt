package services.jda.commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object SpreadsheetCommand: Command() {
    override val self = this
    override val name = "Spreadsheet"
    override val description = "Retrieves the link for the attendance spreadsheet."
    override val identifiers = listOf<String>(
        "spreadsheet",
        "sheet"
    )

    override fun execute(event: MessageReceivedEvent) {
        event.channel.sendMessage("https://docs.google.com/spreadsheets/d/1n5yxhCf6L3UeAEk7SyeoSY0Rc4Ltq63AUnVBJu5HkSw/edit#gid=1473562382").queue()
    }
}