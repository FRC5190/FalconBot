package services.jda.commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import services.JDAService

abstract class Command(
    private val parent: Command? = null,
    val permissionLevel: CommandPermissionLevel = CommandPermissionLevel.ALL,
    val name: String,
    val description: String,
    val ids: List<String>
) {
    var fullIds = mutableListOf<String>()
    val children = mutableListOf<Command>()

    abstract fun execute(event: MessageReceivedEvent, args: List<String>)
    open fun initSubcommands() {}

    fun load() {
        if (parent == null) {
            JDAService.commands.add(this)
            for (identifier in ids) {
                JDAService.commandIds.put(identifier, this)
                fullIds.add(identifier)
            }
        } else {
            parent.children.add(this)
            for (identifier in ids) {
                for (parentId in parent.fullIds) {
                    JDAService.commandIds.put("$parentId $identifier", this)
                    fullIds.add("$parentId $identifier")
                }
            }
        }

        initSubcommands()
    }
}