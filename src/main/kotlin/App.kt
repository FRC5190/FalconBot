import services.JDAService
import services.GoogleSheets

object Application {
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
    }
}

fun main() {
    Application.start()
}