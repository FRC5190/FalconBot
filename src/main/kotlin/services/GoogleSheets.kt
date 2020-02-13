package services

import SheetsConstants
import SheetsConstants.TOKENS_DIRECTORY_PATH
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader

object GoogleSheets {
    val logger = LoggerFactory.getLogger("Application")

    lateinit var service: Sheets

    fun start() {
        logger.info("Starting Sheets API...")

        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()

        service = Sheets.Builder(
            HTTP_TRANSPORT,
            SheetsConstants.JSON_FACTORY,
            getCredentials(HTTP_TRANSPORT)
        )
            .setApplicationName("FRC5190 Discord Bot")
            .build()

        logger.info("Initialized Sheets API")
    }

    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
        val inputStream = this.javaClass.getResourceAsStream(SheetsConstants.CREDENTIALS_FILE_PATH)
            ?: throw FileNotFoundException("Resource not found: " + SheetsConstants.CREDENTIALS_FILE_PATH)

        val clientSecrets = GoogleClientSecrets.load(SheetsConstants.JSON_FACTORY, InputStreamReader(inputStream))

        val flow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT,
            SheetsConstants.JSON_FACTORY,
            clientSecrets,
            SheetsConstants.SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()

        val reciever = LocalServerReceiver.Builder()
            .setPort(8888)
            .build()

        return AuthorizationCodeInstalledApp(flow, reciever).authorize("user");
    }
}