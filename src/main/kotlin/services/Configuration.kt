package services

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import services.configuration.StringListValue
import services.configuration.StringMapValue
import services.configuration.StringValue
import java.io.File

object Configuration {
    var file = File("configuration.json")
    var json = JSONParser().parse(file.readText()) as JSONObject

    var appName: String by StringValue("app_name")
    var appVersion: String by StringValue("app_version")
    var jdaPrefix: String by StringValue("jda_prefix")
    var jdaToken: String by StringValue("jda_token")
    var seasonDate: String by StringValue("season_startdate")
    var sheets: Map<String, String> by StringMapValue("sheets")
    var links: Map<String, String> by StringMapValue("links")
    var owners: List<String> by StringListValue("owners")
    var botChannel: String by StringValue("bot_channel")

    var values = mutableMapOf<String, StringValue>()

    fun save() {
        file.writeText(json.toJSONString())
    }
}