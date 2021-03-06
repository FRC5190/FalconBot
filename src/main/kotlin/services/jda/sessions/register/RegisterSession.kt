package services.jda.sessions.register

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.json.simple.JSONObject
import services.Configuration
import services.jda.CommandListener
import services.jda.commands.register.LegacyCommand
import services.jda.sessions.MessageSession
import services.sheets.Attendance

class RegisterSession : MessageSession() {
    override var data = JSONObject().apply {
        set("status", "start")
        set("private_channel", "0")
        set("name", "")
        set("falcontime", "")
        set("email", "")
        set("gender", "")
        set("role", "")
    }

    override fun execute(event: MessageReceivedEvent, args: List<String>) {
        if (data["status"] == "start") {
            val embed = EmbedBuilder()
                .setTitle("Register")
                .setDescription("If you already have a FalconTime account (the buildspace login system) type `Legacy` \n" +
                        "If you do not have a FalconTime account, type `Continue`")
                .setColor(ColorConstants.FALCON_MAROON)

            data["private_channel"] = event.author.openPrivateChannel().complete().id
            event.jda.getPrivateChannelById(data["private_channel"] as String)!!
                .sendMessage(embed.build()).complete()

            if (event.channelType != ChannelType.PRIVATE) {
                val infoEmbed = EmbedBuilder()
                    .setTitle("Register")
                    .setDescription("Check your DMs!")
                    .setColor(ColorConstants.FALCON_MAROON)

                event.channel.sendMessage(infoEmbed.build()).complete()
            }

            data["status"] = "type"
        } else if (event.channel.id == data["private_channel"] as String) {
            when(data["status"] as String) {
                "type" -> ::typeSelection
                "name" -> ::setName
                "falcontime" -> ::setFalcon
                "email" -> ::setEmail
                "gender" -> ::setGender
                "role" -> ::setRole
                else -> null
            }?.invoke(event, args)
        } else if (event.message.contentRaw.startsWith(Configuration.jdaPrefix)) {
            CommandListener().onGenericCommandReceived(event, args)
        }
    }

    private fun typeSelection(event: MessageReceivedEvent, args: List<String>) {
        when(args[0].toLowerCase()) {
            "continue" -> {
                val embed = EmbedBuilder()
                    .setTitle("Register")
                    .setDescription("You have selected to create a new account. \n" +
                            "What is your name? Type `First, Last`")
                    .setFooter("Example: Bob, Miller")
                    .setColor(ColorConstants.FALCON_MAROON)

                event.channel.sendMessage(embed.build()).complete()

                data["status"] = "name"
            }
            "legacy" -> { LegacyCommand.execute(event, args); this.end() }
            else -> {
                val embed = EmbedBuilder()
                    .setTitle("Error")
                    .setDescription("`legacy` or `continue` was not specified.")
                    .setColor(ColorConstants.FALCON_MAROON)

                event.channel.sendMessage(embed.build()).complete()
            }
        }
    }

    private fun setName(event: MessageReceivedEvent, args: List<String>) {
        data["name"] = event.message.contentRaw.split(',').let {
            if (it.count() == 2) {
                val name = it[0] + "," + it[1].removePrefix(" ")
                val embed = EmbedBuilder()
                    .setTitle("Register")
                    .setDescription("Name set to ${name}. \n" +
                            "Type a number to be used as your FalconTime ID. \n" +
                            "This will be the number you will use to log into the attendance system at the buildspace. \n" +
                            "People most commonly use their phone number for this.")
                    .setFooter("Example: 9195555555")
                    .setColor(ColorConstants.FALCON_MAROON)

                event.channel.sendMessage(embed.build()).complete()
                data["status"] = "falcontime"
                name
            } else {
                val embed = EmbedBuilder()
                    .setTitle("Error")
                    .setDescription("Name provided was not in valid format.")
                    .setFooter("Example: Bob, Miller")
                    .setColor(ColorConstants.FALCON_MAROON)

                event.channel.sendMessage(embed.build()).complete()
                ""
            }
        }
    }

    private fun setFalcon(event: MessageReceivedEvent, args: List<String>) {
        try {
            data["falcontime"] = args[0].toLong()

            if (!Attendance.getMembers().any { member -> member.falconTimeID == data["falcontime"].toString() }) {
                val embed = EmbedBuilder()
                    .setTitle("Register")
                    .setDescription(
                        "FalconTime ID set to ${args[0]}. \n" +
                                "Next, provide your email."
                    )
                    .setFooter("Example: yourname@example.com")
                    .setColor(ColorConstants.FALCON_MAROON)

                event.channel.sendMessage(embed.build()).complete()
                data["status"] = "email"
            } else {
                val embed = EmbedBuilder()
                    .setTitle("Error")
                    .setDescription(
                        "Another member already uses that ID! \n" +
                                "Choose an ID that has not been taken."
                    )
                    .setColor(ColorConstants.FALCON_MAROON)

                event.channel.sendMessage(embed.build()).complete()
            }
        } catch(e: Exception) {
            val embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("FalconTime ID provided was not valid.")
                .setFooter("Example: 9195555555")
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).complete()
        }
    }

    private fun setEmail(event: MessageReceivedEvent, args: List<String>) {
        if (args[0].contains("@")) {
            data["email"] = args[0]

            val embed = EmbedBuilder()
                .setTitle("Register")
                .setDescription("Email set to ${args[0]}. \n" +
                        "Next, provide your gender. Type `Male`, `Female`, or `Other`.")
                .setFooter("Example: other")
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).complete()
            data["status"] = "gender"
        } else {
            val embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("Invalid Email provided.")
                .setFooter("Example: yourname@example.com")
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).complete()
        }
    }

    private fun setGender(event: MessageReceivedEvent, args: List<String>) {
        data["gender"] = when(args[0].toLowerCase()) {
            "male" -> "MALE"
            "female" -> "FEMALE"
            "other" -> "OTHER"
            else -> ""
        }

        if (data["gender"] != "") {
            val embed = EmbedBuilder()
                .setTitle("Register")
                .setDescription("Gender set to ${data["gender"]}. \n" +
                        "Next, tell us your role on the team. Type `Mentor`, `Engineering`, `Programming`, or `Marketing`. \n" +
                        "Here is a little background on what each subteam does.")
                .addField("Mentors", "Mentors are adults on the team who help students learn while also having a fun time themselves.", false)
                .addField("Engineering", "The Engineering subteam primarily focuses on building the robot.", false)
                .addField("Programming", "The Programming subteam programs the robot. " +
                        "This subteam is not recommended if you do not already have prior experience using languages like C#, Kotlin, and Java.", false)
                .addField("Marketing", "The Marketing subteam does everything else that most people do not think of when they think of robotics. " +
                        "They get sponsorships, take photos and videos of our robot and matches, make a cool reveal video for the robot, and more!", false)
                .setFooter("Example: mentor")
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).complete()
            data["status"] = "role"
        } else {
            val embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("Sorry, we do not have that gender in our database. \n" +
                        "Please choose from `Male`, `Female`, or `Other`.")
                .setFooter("Example: other")
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).complete()
        }
    }

    private fun setRole(event: MessageReceivedEvent, args: List<String>) {
        data["role"] = when(args[0].toLowerCase()) {
            "mentor" -> "MENTOR"
            "engineering" -> "ENGINEERING"
            "programming" -> "PROGRAMMING"
            "marketing" -> "MARKETING"
            else -> ""
        }

        if (data["role"] != "") {
            val embed = EmbedBuilder()
                .setTitle("Register")
                .setDescription("Role set to ${data["role"]}. \n" +
                        "Thank you for completing registration.")
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).complete()

            finish(event.author.id)
        } else {
            val embed = EmbedBuilder()
                .setTitle("Error")
                .setDescription("Not a valid role. \n" +
                        "Choose from `Mentor`, `Engineering`, `Programming`, or `Marketing`.")
                .setFooter("Example: mentor")
                .setColor(ColorConstants.FALCON_MAROON)

            event.channel.sendMessage(embed.build()).complete()
        }
    }

    private fun finish(discordID: String) {
        val role = when(data["role"]) {
            "MENTOR" -> "MENTOR"
            else -> "STUDENT"
        }

        val firstName = (data["name"] as String).split(',')[0]
        val lastName = (data["name"] as String).split(',')[1]

        Attendance.addMember(
            discordID, data["falcontime"].toString(), firstName, lastName,
            data["email"] as String, data["gender"] as String, role
        )

        end()
    }
}