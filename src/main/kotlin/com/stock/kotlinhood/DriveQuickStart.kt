package com.stock.kotlinhood

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import org.springframework.util.ResourceUtils
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.GeneralSecurityException


internal object DriveQuickstart {
    private val APPLICATION_NAME = "Google Drive API Java Quickstart"
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private val TOKENS_DIRECTORY_PATH = "tokens"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved credentials/ folder.
     */
    private val SCOPES = listOf(DriveScopes.DRIVE_METADATA_READONLY)
    private const val CREDENTIALS_FILE_PATH = "credentials.json"

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
        // Load client secrets.
//        val `in` = this.javaClass::class.java.getResource(CREDENTIALS_FILE_PATH).openStream()
        val file = ResourceUtils.getFile("classpath:$CREDENTIALS_FILE_PATH")
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(file.inputStream()))

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()
        return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
    }

    @Throws(IOException::class, GeneralSecurityException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // Build a new authorized API client service.
        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        val service = Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build()

        val result = service.files().list()
            .setPageSize(2)
            .setFields("nextPageToken, files(id, name, webContentLink)")
            .execute()
        val files = result.files
        if (files == null || files.isEmpty()) {
            println("No files found.")
        } else {
            println("Files:")
            for (file in files) {
                System.out.printf("%s %s %s\n", file.name, file.id, file.webContentLink)
                printFile(service, fileId = file.id)
                downloadFile(service, file)
            }
        }
    }

    /**
     * Print a file's metadata.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to print metadata for.
     */
    private fun printFile(service: Drive, fileId: String) {

        try {
            val file = service.files().get(fileId).execute()

            System.out.println("Description: " + file.description)
            System.out.println("MIME type: " + file.mimeType)
        } catch (e: IOException) {
            println("An error occurred: $e")
        }

    }

    /**
     * Download a file's content.
     *
     * @param service Drive API service instance.
     * @param file Drive File instance.
     * @return InputStream containing the file's content if successful,
     * `null` otherwise.
     */
    private fun downloadFile(service: Drive, file: File) {
//        service.files().get(file.id).executeAsInputStream().toFile("testt")
//        service.files().get(file.id).get()


    }
}

fun InputStream.toFile(path: String) {
    java.io.File(path).outputStream().use { this.copyTo(it) }
}