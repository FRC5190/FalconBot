package services

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

object Configuration {
    lateinit var json: JSONObject
    lateinit var appName: String
    lateinit var jdaPrefix: String
    lateinit var timeSheet: String
    lateinit var userSheet: String
    lateinit var restartChannel: String

    fun load() {
        var parser = JSONParser()
        var reader = FileReader("configuration.json")
        var obj = parser.parse(reader) as JSONArray
        json = obj.get(0) as JSONObject

        appName = json["app_name"].toString()
        jdaPrefix = json["jda_prefix"].toString()
        timeSheet = json["time_sheet"].toString()
        userSheet = json["user_sheet"].toString()

        restartChannel = if (json.containsKey("restart_channel")) {
            json["restart_channel"].toString()
        } else { "" }
    }
}