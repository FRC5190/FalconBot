import services.JDAService
import services.GoogleSheets
import java.util.*

object Application {
    var startTime: Long = 0

    fun start() {
        JDAService.start()
        GoogleSheets.start()

        println("< JDA > \n" +
                "   > Name: ${JDAService.service.selfUser.name} \n" +
                "   > Status: ${JDAService.service.status} \n" +
                "   > Gateway Ping: ${JDAService.service.gatewayPing} \n" +
                "   > Guilds: ${JDAService.service.guilds.count()} \n")

        println("< Google Sheets > \n" +
                "   > Name: ${GoogleSheets.service.applicationName} \n" +
                "   > Url: ${GoogleSheets.service.baseUrl} \n")

        val calendar = Calendar.getInstance()
        startTime = calendar.timeInMillis
    }

    fun getTime(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }
}

fun main() {
    Application.start()
}