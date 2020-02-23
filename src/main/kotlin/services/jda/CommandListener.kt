package services.jda

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent
import net.dv8tion.jda.api.events.emote.update.GenericEmoteUpdateEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import services.Configuration
import services.JDAService
import services.jda.commands.CommandPermissionLevel
import services.jda.sessions.SessionHandler
import java.io.File

class CommandListener : ListenerAdapter() {
    private val logger = LoggerFactory.getLogger("Application")

    override fun onReady(event: ReadyEvent) {
        if (File("temp.json").exists()) {
            var embed = EmbedBuilder()
                .setTitle("Restarted!")
                .setDescription("Bot was restarted.")
                .setColor(ColorConstants.FALCON_MAROON)
                .build()

            var channel = Configuration.botChannel

            if (event.jda.users.any {it.id == channel}) {
                event.jda.getUserById(channel)!!.openPrivateChannel().complete().sendMessage(embed).queue()
            }

            if (event.jda.textChannels.any {it.id == channel}) {
                event.jda.getTextChannelById(channel)!!.sendMessage(embed).queue()
            }
        }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        onGenericMessageReceived(MessageReceivedEvent(event.jda, event.responseNumber, event.message))
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        onGenericMessageReceived(MessageReceivedEvent(event.jda, event.responseNumber, event.message))
    }

    private fun onGenericMessageReceived(event: MessageReceivedEvent) {
        val content = event.message.contentRaw.removePrefix(Configuration.jdaPrefix).toLowerCase().split(' ')

        if (!event.author.isBot) {
            if (SessionHandler.contains(event.author)) {
                SessionHandler.get(event.author)!!.invoke(event, content)
            } else if (event.message.contentRaw.startsWith(Configuration.jdaPrefix)) {
                onGenericCommandReceived(event, content)
            }
        }
    }

    fun onGenericCommandReceived(event: MessageReceivedEvent, content: List<String>){
        for (i in content.count() downTo  0) {
            if (i == 0) {
                event.channel.sendMessage("Command `${event.message.contentRaw}` not found.").queue()
                break
            }

            var stringCommand = content.take(i).fold("", {acc, s -> "$acc$s "}).removeSuffix(" ")
            JDAService.commandIds[stringCommand]?.let {
                if (Configuration.owners.contains(event.author.id) || it.permissionLevel == CommandPermissionLevel.ALL) {
                    it.execute(event, content)
                }
            } ?: continue
            logger.info("${event.author.name} executed \"${event.message.contentRaw}\"")
            break
        }
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val message = event.channel.history.getMessageById(event.messageId)!!
        if (!event.user!!.isBot && SessionHandler.contains(message)) {
            SessionHandler.get(message)!!.let {
                if (it.reactions.contains(event.reactionEmote)) {
                    it.onReactionAdd(event)
                }
            }
        }
    }

    override fun onMessageReactionRemove(event: MessageReactionRemoveEvent) {
        val message = event.channel.history.getMessageById(event.messageId)!!
        if (!event.user!!.isBot && SessionHandler.contains(message)) {
            SessionHandler.get(message)!!.let {
                if (it.reactions.contains(event.reactionEmote)) {
                    it.onReactionRemove(event)
                }
            }
        }
    }
}