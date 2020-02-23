package services.jda.sessions

import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.json.simple.JSONObject

abstract class MessageSession {
    abstract var data: JSONObject

    fun invoke(event: MessageReceivedEvent, args: List<String>) {
        // save session data
        execute(event, args)
    }

    fun end() = SessionHandler.delete(this)
    fun attach(user: User) = SessionHandler.attach(user, this)

    protected abstract fun execute(event: MessageReceivedEvent, args: List<String>)
}
