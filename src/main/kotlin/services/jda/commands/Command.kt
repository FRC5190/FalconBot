package services.jda.commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDA

abstract class Command() {
    abstract val self: Command
    abstract val identifiers: List<String>
    abstract fun execute(event: MessageReceivedEvent)

    fun load() {
        for (identifier in identifiers) {
            JDA.commands.put(identifier, self)
        }
    }
}