package services.jda.sessions

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object SessionHandler {
    private val messageSessions = mutableMapOf<User, MessageSession>()
    private val reactionSessions =  mutableMapOf<Message, ReactionSession>()

    fun attach(user: User, session: MessageSession) {
        messageSessions[user] = session
    }

    fun attach(message: Message, session: ReactionSession) {
        reactionSessions[message] = session
    }

    fun delete(messageSession: MessageSession) {
        messageSessions.forEach { (user, session) ->
            if ( session == messageSession ) {
                messageSessions.remove(user)
            }
        }
    }

    fun delete(reactionSession: ReactionSession) {
        reactionSessions.forEach { (user, session) ->
            if ( session == reactionSession ) {
                reactionSessions.remove(user)
            }
        }
    }

    fun contains(user: User): Boolean = messageSessions.contains(user)
    fun contains(message: Message): Boolean = reactionSessions.contains(message)

    fun get(user: User): MessageSession? = messageSessions[user]
    fun get(message: Message): ReactionSession? = reactionSessions[message]
}