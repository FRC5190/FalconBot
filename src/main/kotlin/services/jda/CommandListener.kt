package services.jda

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import services.JDAService
import java.io.FileWriter

class CommandListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        if (JDAService.credentials.containsKey("restartchannel")) {
            var restartChannelId = JDAService.credentials["restartchannel"]!!
            var restartChannel = JDAService.service.getTextChannelById(restartChannelId as String)!!
            JDAService.credentials.remove("restartchannel")
            FileWriter("jdacredentials.json").use { out -> out.write(JDAService.credentials.toJSONString()) }

            var embed = EmbedBuilder()
                .setTitle("Restarted!")
                .setDescription("Bot was restarted from this channel.")
                .setColor(ColorConstants.FALCON_MAROON)

            restartChannel.sendMessage(embed.build()).queue()
        }
    }

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
        val content = event.message.contentRaw.removePrefix(JDAConstants.kPrefix.toString()).toLowerCase().split(' ')

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