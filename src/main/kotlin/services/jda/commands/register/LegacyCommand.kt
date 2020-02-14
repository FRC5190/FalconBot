package services.jda.commands.register

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.GoogleSheets
import services.jda.commands.Command

object LegacyCommand : Command(
    parent = RegisterCommand,
    name = "Register Legacy",
    description = "Links a FalconTime account with a Discord account.",
    ids = listOf(
        "legacy"
    )
){
    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (args.count() == 2) {
            event.channel.sendMessage("No FalconTime ID was provided.")
        } else {
            val data = GoogleSheets.service.spreadsheets().values()
                .get(SheetsConstants.falconusersSheet, "Sheet1!A2:B1000")
                .execute()

            var values = data.getValues()
            var targetRow =  if (values.any { it[0] == args[2] }) {
                values.indexOf(values.find { it[0] == args[2] }!!)
            } else {
                values.count() + 1
            }

            val userData = GoogleSheets.service.spreadsheets().values()
                .get(SheetsConstants.falcontimeSheet, "Current!A2:J1000")
                .execute()

            val userValues = userData.getValues()
            if (userValues.any { it[0] == args[2] }) {
                values.add(targetRow, listOf(args[2], event.author.id))
                data.setValues(values)
                GoogleSheets.service.spreadsheets().values()
                    .update(SheetsConstants.falconusersSheet, "Sheet1!A2:B1000", data)
                    .execute()

                event.channel.sendMessage("FalconTime account and Discord account linked!")
            } else {
                event.channel.sendMessage("A FalconTime account was not found under that id.")
            }
        }
    }
}