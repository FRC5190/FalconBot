package services.jda

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import services.JDAService
import sun.plugin2.message.Message

class CommandListener : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (!event.author.isBot && event.message.contentRaw != null && event.message.contentRaw[0] == JDAConstants.kPrefix) {
            onGenericCommandReceived(MessageReceivedEvent(event.jda, event.responseNumber, event.message))
        }
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        if (!event.author.isBot && event.message.contentRaw != null && event.message.contentRaw[0] == JDAConstants.kPrefix) {
            onGenericCommandReceived(MessageReceivedEvent(event.jda, event.responseNumber, event.message))
        }
    }

    private fun onGenericCommandReceived(event: MessageReceivedEvent){
        val content = event.message.contentRaw.removePrefix(JDAConstants.kPrefix.toString()).split(' ')

        for (i in content.count() downTo  0) {
            if (i == 0) {
                event.channel.sendMessage("Command `${event.message.contentRaw}` not found.").queue()
                break
            }

            var stringCommand = content.take(i).fold("", {acc, s -> "$acc$s "}).removeSuffix(" ")
            JDAService.commandIds[stringCommand]
                ?.execute(event, content) ?: continue
            break
        }
    }
}