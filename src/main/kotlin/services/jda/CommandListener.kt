package services.jda

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import services.JDA

class CommandListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("< JDA > \n" +
                "   > Name: ${JDA.service.selfUser.name} \n" +
                "   > Status: ${JDA.service.status} \n" +
                "   > Gateway Ping: ${JDA.service.gatewayPing} \n" +
                "   > Guilds: ${JDA.service.guilds.count()} \n")
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        println(event.message.contentRaw)
        if (event.message.contentRaw[0] == JDAConstants.kPrefix) {
            val parts = event.message.contentRaw.removePrefix(JDAConstants.kPrefix.toString()).split(' ')
            JDA.commands[parts[0].toLowerCase()]!!.execute(event as MessageReceivedEvent)
        }
    }
}