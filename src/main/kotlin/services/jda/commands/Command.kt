package services.jda.commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDA

abstract class Command(val command: Command) {
    abstract val identifiers: List<String>
    abstract fun execute(event: MessageReceivedEvent)

    init {
        for (identifier in identifiers) {
            JDA.commands.put(identifier, command)
        }
    }
}