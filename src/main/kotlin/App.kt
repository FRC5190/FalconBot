import services.JDAService
import services.GoogleSheets
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

object Application {
    lateinit var startTime: LocalDateTime

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
        startTime = LocalDateTime.now()
    }
}

fun main() {
    Application.start()
}