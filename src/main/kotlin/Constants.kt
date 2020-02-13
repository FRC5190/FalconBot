import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.SheetsScopes
import java.awt.Color

object ColorConstants {
    val FALCON_MAROON = Color(104, 10, 15)
}

object SheetsConstants {
    const val APPLICATION_NAME = "FRC5190 Discord Bot"
    const val TOKENS_DIRECTORY_PATH = "tokens"
    const val CREDENTIALS_FILE_PATH = "/googlecredentials.json"

    val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    val SCOPES = listOf<String>(SheetsScopes.SPREADSHEETS)

    const val falcontimeSheet = "17MEtLnRMXNj-H980Tc0FBPgbv8XXpVrLAPyxuri__Eg"
    const val falconusersSheet = "1tl2eNYY_CGbsD_0PlCPyt6rcETVHt5KNQEG0G6RePI4"
}

object JDAConstants {
    const val CREDENTIALS_FILE_PATH = "/jdacredentials.json"

    const val kPrefix = '!'
}