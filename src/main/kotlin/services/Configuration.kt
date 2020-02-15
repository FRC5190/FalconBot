package services

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader

object Configuration {
    lateinit var file: File
    lateinit var json: JSONObject

    lateinit var appName: String
    lateinit var appVersion: String
    lateinit var jdaPrefix: String
    lateinit var jdaToken: String
    lateinit var sheets: Map<String, String>
    lateinit var links: Map<String, String>
    lateinit var owners: List<String>

    fun load() {
        file = File("configuration.json")
        json = JSONParser().parse(file.readText()) as JSONObject

        appName = json["app_name"] as String
        appVersion = json["app_version"] as String
        jdaPrefix = json["jda_prefix"] as String
        jdaToken = json["jda_token"] as String
        sheets = json["sheets"] as Map<String, String>
        links = json["links"] as Map<String, String>
        owners = json["owners"] as List<String>
    }

    fun setValue(key: String, value: Any?) {
        json[key] = value
        file.writeText(json.toJSONString())
    }
}