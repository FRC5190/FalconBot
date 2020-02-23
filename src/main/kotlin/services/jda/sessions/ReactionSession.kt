package services.jda.sessions

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageReaction
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent

abstract class ReactionSession(
    val reactions: List<MessageReaction.ReactionEmote>
) {
    abstract fun onReactionAdd(event: MessageReactionAddEvent)
    abstract fun onReactionRemove(event: MessageReactionRemoveEvent)

    fun end() = SessionHandler.delete(this)
    fun attach(message: Message) = SessionHandler.attach(message, this)
}