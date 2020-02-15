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
    lateinit var restartMessage: String

    fun load() {
        var reader = FileReader("configuration.json")
        json = JSONParser().parse(reader) as JSONObject

        appName = json["app_name"].toString()
        jdaPrefix = json["jda_prefix"].toString()
        timeSheet = json["time_sheet"].toString()
        userSheet = json["user_sheet"].toString()

        restartChannel = json["restart_channel"]?.toString()
            ?: ""

        restartMessage = json["restart_message"]?.toString()
            ?: ""
    }
}