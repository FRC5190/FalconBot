package services.jda

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import services.JDA

class CommandListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {

    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        println(event.message.contentRaw)
        if (event.message.contentRaw[0] == JDAConstants.kPrefix) {
            val parts = event.message.contentRaw.removePrefix(JDAConstants.kPrefix.toString()).split(' ')
            println(parts[0])
            JDA.commands[parts[0].toLowerCase()]!!.execute(MessageReceivedEvent(event.jda, event.responseNumber, event.message))
        }
    }
}