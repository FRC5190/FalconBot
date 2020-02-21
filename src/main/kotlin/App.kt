import services.Configuration
import services.JDAService
import services.GoogleSheets
import java.time.LocalDateTime

object Application {
    lateinit var startTime: LocalDateTime

    fun start() {
        Configuration
        JDAService.start()
        GoogleSheets.start()

        println("\n< JDA > \n" +
                "   > Name: ${JDAService.service.selfUser.name} \n" +
                "   > Status: ${JDAService.service.status} \n" +
                "   > Gateway Ping: ${JDAService.service.gatewayPing} \n" +
                "   > Guilds: ${JDAService.service.guilds.count()} \n")

        println("< Google Sheets > \n" +
                "   > Name: ${GoogleSheets.service.applicationName} \n" +
                "   > Url: ${GoogleSheets.service.baseUrl} \n")

        startTime = LocalDateTime.now()
    }
}

fun main() {
    Application.start()
}