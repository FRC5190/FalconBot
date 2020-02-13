package services.jda.commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDAService

abstract class Command() {
    abstract val self: Command
    abstract val name: String
    abstract val description: String
    abstract val identifiers: List<String>

    abstract fun execute(event: MessageReceivedEvent)

    fun load() {
        JDAService.commands.add(self)
        for (identifier in identifiers) {
            JDAService.commandIdentifiers.put(identifier, self)
        }
    }
}