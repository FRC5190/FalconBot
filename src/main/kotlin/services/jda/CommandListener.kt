package services.jda

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageType
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.json.simple.JSONArray
import services.Configuration
import services.JDAService
import java.io.File
import java.io.FileWriter

class CommandListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        if (Configuration.restartChannel != "") {
            var embed = EmbedBuilder()
                .setTitle("Restarted!")
                .setDescription("Bot was restarted from this channel.")
                .setColor(ColorConstants.FALCON_MAROON)

            JDAService.service.getTextChannelById(Configuration.restartChannel)!!.sendMessage(embed.build()).queue()

            Configuration.json.remove("restart_channel")
            var file = FileWriter("configuration.json")
            file.write(Configuration.json.toJSONString())
            file.flush()
        }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (!event.author.isBot && event.message.contentRaw.startsWith(Configuration.jdaPrefix)) {
            onGenericCommandReceived(MessageReceivedEvent(event.jda, event.responseNumber, event.message))
        }
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        if (!event.author.isBot && event.message.contentRaw.startsWith(Configuration.jdaPrefix)) {
            onGenericCommandReceived(MessageReceivedEvent(event.jda, event.responseNumber, event.message))
        }
    }

    private fun onGenericCommandReceived(event: MessageReceivedEvent){
        val content = event.message.contentRaw.removePrefix(Configuration.jdaPrefix).toLowerCase().split(' ')

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