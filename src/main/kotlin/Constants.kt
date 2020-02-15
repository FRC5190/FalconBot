import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.SheetsScopes
import java.awt.Color

object ColorConstants {
    val FALCON_MAROON = Color(104, 10, 15)
}

object SheetsConstants {
    const val TOKENS_DIRECTORY_PATH = "tokens"
    const val CREDENTIALS_FILE_PATH = "/googlecredentials.json"

    val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    val SCOPES = listOf<String>(SheetsScopes.SPREADSHEETS)
}

object JDAConstants {
    const val CREDENTIALS_FILE_PATH = "/jdacredentials.json"
}