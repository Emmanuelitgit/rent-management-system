package com.rent_management_system.fileManager;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class GoogleDriveService {
    private static final String APPLICATION_NAME = "Google Drive API Java";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String GOOGLE_CREDENTIALS_PATH = System.getenv("GOOGLE_CREDENTIALS_PATH");
    private static final String FOLDER_ID = System.getenv("FOLDER_ID");

    public static void main(String[] args) {
        if (FOLDER_ID == null) {
            throw new IllegalStateException("FOLDER_ID environment variable is not set.");
        }

        System.out.println("Google Drive Folder ID: " + FOLDER_ID);
    }

    private Drive driveService;

    public GoogleDriveService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        this.driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(getCredentials()))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private GoogleCredentials getCredentials() throws IOException {
        // Get the JSON string from environment variables
        String credentialsJson = System.getenv("GOOGLE_CREDENTIALS_JSON");

        if (credentialsJson == null || credentialsJson.isEmpty()) {
            throw new IllegalStateException("GOOGLE_CREDENTIALS_JSON environment variable is not set.");
        }

        // Create a temporary file to store the credentials
        Path tempFilePath = Files.createTempFile("gcp-credentials", ".json");
        Files.write(tempFilePath, credentialsJson.getBytes(), StandardOpenOption.WRITE);

        // Load credentials from the temporary file
        try (InputStream serviceAccountStream = Files.newInputStream(tempFilePath)) {
            return GoogleCredentials.fromStream(serviceAccountStream)
                    .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive"));
        }
    }

//    private GoogleCredentials getCredentials() throws IOException {
//        ClassLoader classLoader = getClass().getClassLoader();
//        InputStream serviceAccountStream = classLoader.getResourceAsStream("credentials.json");
//
//        if (serviceAccountStream == null) {
//            throw new IllegalStateException("Could not find credentials.json in resources folder.");
//        }
//
//        return GoogleCredentials.fromStream(serviceAccountStream)
//                .createScoped(Collections.singleton(DriveScopes.DRIVE));
//    }


//    private GoogleCredentials getCredentials() throws IOException {
//        if (GOOGLE_CREDENTIALS_PATH == null || GOOGLE_CREDENTIALS_PATH.isEmpty()) {
//            throw new IllegalStateException("GOOGLE_CREDENTIALS_PATH environment variable is not set.");
//        }
//
//        InputStream serviceAccountStream = new FileInputStream(GOOGLE_CREDENTIALS_PATH);
//        return GoogleCredentials.fromStream(serviceAccountStream)
//                .createScoped(Collections.singleton(DriveScopes.DRIVE));
//    }

    public String uploadFile(MultipartFile file) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        FileContent mediaContent = new FileContent(file.getContentType(), convertMultiPartToFile(file));
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        return uploadedFile.getId();
    }

    public String updateFileById(String fileId, MultipartFile file) throws IOException {
        java.io.File tempFile = convertMultiPartToFile(file);
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());

        FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
        File updatedFile = driveService.files()
                .update(fileId, fileMetadata, mediaContent)
                .setFields("id, name, modifiedTime")
                .execute();

        tempFile.delete();
        return updatedFile.getId();
    }

    public void removeFileById(String fileId) throws IOException {
        driveService.files().delete(fileId).execute();
    }

    private java.io.File convertMultiPartToFile(MultipartFile file) throws IOException {
        java.io.File convFile = new java.io.File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    public List<File> listFiles() throws IOException {
        FileList result = driveService.files().list()
                .setQ("'" + FOLDER_ID + "' in parents and mimeType contains 'image/'")
                .setPageSize(20)
                .setFields("nextPageToken, files(id, name, mimeType, webViewLink, webContentLink, thumbnailLink)")
                .execute();
        return result.getFiles();
    }
}




//package com.rent_management_system.fileManager;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.FileContent;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.Value;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.FileList;
//import com.google.auth.oauth2.GoogleCredentials;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.List;
//
//@Slf4j
//@Service
//public class GoogleDriveService {
//    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
//    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
//    private static final String FOLDER_ID = "1btkH3YpRpKZzOEmFCSOao3w6vl1HsunP";
//
//    private Drive driveService;
//
//    public GoogleDriveService() throws GeneralSecurityException, IOException {
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        this.driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//
//
//    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
//        InputStream in = GoogleDriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in == null) {
//            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//        }
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(5000).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//    }
//
//    // for uploading image/file to google drive which will return the file id
//    public String uploadFile(MultipartFile file) throws IOException {
//        File fileMetadata = new File();
//        fileMetadata.setName(file.getOriginalFilename());
//        fileMetadata.setParents(Collections.singletonList(FOLDER_ID)); // Save in a specific folder
//
//        FileContent mediaContent = new FileContent(file.getContentType(), convertMultiPartToFile(file));
//        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
//                .setFields("id")
//                .execute();
//        return uploadedFile.getId(); // Save this ID in the database
//    }
//
//    public String updateFileById(String fileId, MultipartFile file) throws IOException {
//
//        java.io.File tempFile = convertMultiPartToFile(file);
//        File fileMetadata = new File();
//        fileMetadata.setName(file.getOriginalFilename());
//        log.info("LOGGING FILE:==={}", file.getOriginalFilename());
//        FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
//        File updatedFile = driveService.files()
//                .update(fileId, fileMetadata, mediaContent)
//                .setFields("id, name, modifiedTime")
//                .execute();
//
//        tempFile.delete();
//
//        return updatedFile.getId();
//    }
//
//
//    public void removeFileById(String fileId) throws IOException {
//        driveService.files().delete(fileId).execute();
//    }
//
//    // Convert MultipartFile to a File
//    private java.io.File convertMultiPartToFile(MultipartFile file) throws IOException {
//        java.io.File convFile = new java.io.File(file.getOriginalFilename());
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(file.getBytes());
//        fos.close();
//        return convFile;
//    }
//
//
//    public List<File> listFiles() throws IOException {
//        FileList result = driveService.files().list()
//                .setQ("'" + FOLDER_ID + "' in parents and mimeType contains 'image/'")
//                .setPageSize(20)
//                .setFields("nextPageToken, files(id, name, mimeType, webViewLink, webContentLink, thumbnailLink)")
//                .execute();
//        return result.getFiles();
//    }
//
//}