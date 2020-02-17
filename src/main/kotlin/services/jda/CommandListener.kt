package services.jda

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import services.Configuration
import services.JDAService
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime

class CommandListener : ListenerAdapter() {
    private val logger = LoggerFactory.getLogger("Application")

    override fun onReady(event: ReadyEvent) {
        if (File("temp.json").exists()) {
            var embed = EmbedBuilder()
                .setTitle("Restarted!")
                .setDescription("Bot was restarted from this channel.")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            var channel = (JSONParser().parse(File("temp.json").readText()) as JSONObject)["restart_channel"] as String

            if (event.jda.users.any {it.id == channel}) {
                event.jda.getUserById(channel)!!.openPrivateChannel().complete().sendMessage(embed).queue()
            }

            if (event.jda.textChannels.any {it.id == channel}) {
                event.jda.getTextChannelById(channel)!!.sendMessage(embed).queue()
            }

            File("temp.json").delete()
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
            JDAService.commandIds[stringCommand]?.execute(event, content) ?: continue
            logger.info("${event.author.name} executed \"${event.message.contentRaw}\"")
            break
        }
    }
}