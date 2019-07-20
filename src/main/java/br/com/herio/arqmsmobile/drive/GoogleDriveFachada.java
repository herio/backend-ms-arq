package br.com.herio.arqmsmobile.drive;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import br.com.herio.arqmsmobile.service.FileStorageService;

@Component
public class GoogleDriveFachada {
	// https://developers.google.com/drive/api/v3/quickstart/java?authuser=1
	private static final String APPLICATION_NAME = "juris-apps";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	private static final Set<String> SCOPES = DriveScopes.all();
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	private Drive service;

	@Autowired
	protected FileStorageService fileStorageService;

	public GoogleDriveFachada() {
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			this.service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME)
					.build();
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException("Erro ao conectar ao Drive", e);
		}
	}

	public List<File> listFiles() {
		try {
			return service.files().list()
					.setFields("nextPageToken, files(id, name)")
					.execute().getFiles();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public File uploadFile(MultipartFile mFile, String idFolder) {
		try {
			java.io.File iofile = fileStorageService.storeFile(mFile);
			File fileMetadata = new File();
			fileMetadata.setName(iofile.getName());
			fileMetadata.setParents(Collections.singletonList(idFolder));
			FileContent mediaContent = new FileContent("image/" + iofile.getName().split("\\.")[1], iofile);

			// upload
			return service.files().create(fileMetadata, mediaContent)
					.setFields("name,id,parents,webViewLink")
					.execute();
		} catch (IOException e) {
			throw new RuntimeException("Erro em uploadFile", e);
		}
	}

	public java.io.File downloadFile(String fileId, String fileName) {
		try {
			java.io.File file = new java.io.File(fileName);
			FileOutputStream outputStream = new FileOutputStream(file);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			service.files().get(fileId).executeMediaAndDownloadTo(byteArrayOutputStream);
			byteArrayOutputStream.writeTo(outputStream);
			return file;
		} catch (IOException e) {
			throw new RuntimeException("Erro em downloadFile", e);
		}
	}

	private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		InputStream in = GoogleDriveFachada.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.build();
		return flow.loadCredential("user");
	}

	public static void main(String... args) {
		try {
			GoogleDriveFachada fachadaGoogleDrive = new GoogleDriveFachada();
			List<File> files = fachadaGoogleDrive.listFiles();
			if (files == null || files.isEmpty()) {
				System.out.println("No files found.");
			} else {
				System.out.println("Files:");
				for (File file : files) {
					System.out.printf("file[%s],[%s]\n", file.getName(), file.getId());
				}
			}

			String fileName = "AMODIREITO.png";
			// file to upload
			URL resource = GoogleDriveFachada.class.getResource("/static/publico/" + fileName);
			java.io.File file = new java.io.File(resource.getFile());

			// dados upload
			FileInputStream input = new FileInputStream(file);
			MultipartFile mfile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
			String folderUploads = "1qk7108N-6xW613ez3abtPfDiWahYnJ4E";
			File fileUpload = fachadaGoogleDrive.uploadFile(mfile, folderUploads);
			if (fileUpload != null) {
				System.out.println(String.format("fileUpload[%s],[%s], [%s]", fileUpload.getName(),
						fileUpload.getId(), fileUpload.getParents()));

				java.io.File fileDownload = fachadaGoogleDrive.downloadFile(fileUpload.getId(), "imagem.png");
				if (fileDownload != null) {
					System.out.println(String.format("fileDownload[%s]", fileDownload.getName()));
				} else {
					System.out.println("fileDownload null");
				}
			} else {
				System.out.println("fileUpload null");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}