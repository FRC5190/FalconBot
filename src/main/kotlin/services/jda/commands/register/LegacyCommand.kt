package services.jda.commands.register

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.GoogleSheets
import services.jda.commands.Command

object LegacyCommand : Command(
    parent = RegisterCommand,
    name = "Register Legacy",
    description = "Links a FalconHours account with a Discord account.",
    ids = listOf(
        "legacy"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (args.count() == 2) {
            event.channel.sendMessage("No FalconHours ID was provided.")
        } else {
            val data = GoogleSheets.service.spreadsheets().values()
                .get(SheetsConstants.falcontimeSheet, "Current!A2:L1000")
                .execute()

            var values = data.getValues()
            var targetRow = 0
            for (i in 0 until values.count() - 1) {
                if (values[i][0] == "") {
                    targetRow = i
                    break
                }
            }
        }
    }
}