package br.com.herio.arqmsmobile.infra.drive;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import br.com.herio.arqmsmobile.dominio.ArquivoUsuario;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.service.FileStorageService;

@Component
public class GoogleDriveFachada {
	// https://developers.google.com/identity/sign-in/web/sign-in#before_you_begin
	// https://developers.google.com/drive/api/v3/quickstart/java?authuser=1
	private static final String URL_BASE_LOCALHOST = "http://192.168.0.12:";

	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDriveFachada.class);
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	private static final Set<String> SCOPES = DriveScopes.all();

	protected Drive service;

	@Value("${googledrive.appName}")
	protected String appName;

	@Value("${googledrive.credentialsFile}")
	protected String credentialsFile;

	@Autowired
	protected Environment env;

	@Autowired
	protected ImageResizer imageResizer;

	@Autowired
	protected FileStorageService fileStorageService;

	@PostConstruct
	public void init() {
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Credential credential = getCredentials(HTTP_TRANSPORT);
			if (credential == null) {
				LOGGER.error("GoogleDriveFachada credential == null");
			} else {
				this.service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(appName).build();
				LOGGER.info("GoogleDriveFachada credential carregada com sucesso");
			}
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException("GoogleDriveFachada Erro ao conectar ao Drive", e);
		}
	}

	public List<File> listFiles(String idDirRaiz) throws IOException {
		return listFiles(idDirRaiz, "nextPageToken, files(id, name, parents)");
	}

	public List<File> listFiles(String idDirRaiz, String fields) throws IOException {
		List<File> allFiles = new ArrayList<>();
		Files.List request = service.files().list().setQ(String.format("trashed = false and '%s' in parents", idDirRaiz)).setFields(fields)
				.setPageToken(null);
		do {
			FileList files = request.execute();
			allFiles.addAll(files.getFiles());
			request.setPageToken(files.getNextPageToken());
		} while (request.getPageToken() != null);
		return allFiles;
	}

	public List<File> listFilesNomeDir(String idDirRaiz, String nomeDir) {
		try {
			File dirPesquisado = recuperaDiretorio(idDirRaiz, nomeDir);
			if (dirPesquisado == null) {
				return null;
			} else {
				String idDirPesquisado = dirPesquisado.getId();
				return service.files().list().setQ(String.format("'%s' in parents", idDirPesquisado)).setSpaces("drive")
						.setFields("files(id, name, parents)").execute().getFiles();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<File> listFilesDirUsuario(String idFolder, String idDirUsuario) {
		try {
			String idFolderPesquisa = idFolder;
			if (idDirUsuario != null) {
				idFolderPesquisa = recuperaDiretorioUsuario(idFolder, idDirUsuario).getId();
			}
			return service.files().list().setQ(String.format("'%s' in parents", idFolderPesquisa)).setSpaces("drive")
					.setFields("files(id, name, parents)").execute().getFiles();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public File getFolder(String idFolder) {
		try {
			return service.files().get(idFolder).execute();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void uploadFile(Long idUsuario, MultipartFile mFile, String idFolder, Usuario usuario, EnumSistema sistema) {
		uploadFile(idUsuario, mFile, idFolder, null, usuario, sistema);
	}

	public void uploadFile(Long idUsuario, MultipartFile mFile, String idFolder, ArquivoUsuario arquivo, EnumSistema sistema) {
		uploadFile(idUsuario, mFile, idFolder, arquivo, null, sistema);
	}

	protected void uploadFile(Long idUsuario, MultipartFile mFile, String idFolder, ArquivoUsuario arquivo, Usuario usuario, EnumSistema sistema) {
		try {
			File diretorioUsuario = recuperaDiretorioUsuario(idFolder, idUsuario.toString());
			String mimeType = new Tika().detect(mFile.getOriginalFilename());

			// gFile
			java.io.File file = null;
			if (mimeType != null && mimeType.contains("image")) {
				file = imageResizer.salvaLocaleRedimensiona(mFile, 80);
			} else {
				file = fileStorageService.storeFile(mFile);
			}
			File fileMetadata = new File();
			fileMetadata.setName(file.getName());
			fileMetadata.setParents(Collections.singletonList(diretorioUsuario.getId()));
			FileContent mediaContent = new FileContent(mimeType, file);
			File gFile = service.files().create(fileMetadata, mediaContent).setFields("id, name, parents, webViewLink").execute();

			String urlDownloadArquivo = getUrlBase(sistema) + "/publico/usuarios/%s/files/arquivos/%s";
			String urlDownloadUsuario = getUrlBase(sistema) + "/publico/usuarios/%s/files/fotos";
			if (arquivo != null) {
				String fileUri = String.format(urlDownloadArquivo, idUsuario, gFile.getId());
				arquivo.setIdDrive(gFile.getId());
				arquivo.setNome(gFile.getName());
				arquivo.setLink(fileUri);
			}
			if (usuario != null) {
				String fileUri = String.format(urlDownloadUsuario, idUsuario);
				usuario.setUrlFoto(fileUri);
				usuario.setIdDriveFoto(gFile.getId());
			}

			// gFileThumb
			if (mimeType != null && mimeType.contains("image")) {
				// redimensiona e salva imagem
				java.io.File fileThumb = imageResizer.salvaLocaleRedimensiona(mFile, 20);
				File fileMetadataThumb = new File();
				fileMetadataThumb.setName("thumb" + fileThumb.getName());
				fileMetadataThumb.setParents(Collections.singletonList(diretorioUsuario.getId()));
				FileContent mediaContentThumb = new FileContent(mimeType, fileThumb);
				File gFileThumb = service.files().create(fileMetadataThumb, mediaContentThumb).setFields("id, name, parents, webViewLink").execute();
				if (arquivo != null) {
					String fileUriThumb = String.format(urlDownloadArquivo, idUsuario, gFile.getId()) + "?thumb=true";
					arquivo.setIdDriveThumb(gFileThumb.getId());
					arquivo.setLinkThumb(fileUriThumb);
				}
				if (usuario != null) {
					String fileUriThumb = String.format(urlDownloadUsuario, idUsuario) + "?thumb=true";
					usuario.setUrlFotoThumb(fileUriThumb);
					usuario.setIdDriveFotoThumb(gFileThumb.getId());
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("GoogleDriveFachada Erro em uploadFile", e);
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
			throw new RuntimeException("GoogleDriveFachada Erro em downloadFile", e);
		}
	}

	public boolean deleteFile(String fileId) {
		try {
			service.files().delete(fileId).execute();
			return true;
		} catch (IOException e) {
			throw new RuntimeException("GoogleDriveFachada Erro em deleteFile", e);
		}
	}

	protected File recuperaDiretorio(String idDirRaiz, String nomeDir) throws IOException {
		File diretorioUsuario = null;
		String pageToken = null;
		do {

			Drive.Files.List files = service.files().list();
			if (nomeDir == null) {
				files = files.setQ(String.format("'%s' in parents", idDirRaiz));
			} else {
				files = files.setQ(String.format("name='%s' and '%s' in parents", nomeDir, idDirRaiz));
			}

			FileList result = files.setSpaces("drive").setFields("nextPageToken, files(id, name, parents)").setPageToken(pageToken).execute();
			if (result != null && result.getFiles() != null && result.getFiles().size() == 1) {
				// encontrou diret�rio usu�rio
				diretorioUsuario = result.getFiles().get(0);
				break;
			}
			pageToken = result.getNextPageToken();
		} while (pageToken != null);

		return diretorioUsuario;
	}

	protected File recuperaDiretorioUsuario(String idFolder, String idDirUsuario) throws IOException {
		File diretorioUsuario = null;
		String pageToken = null;
		do {
			FileList result = service.files().list().setQ(String.format("name='%s' and '%s' in parents", idDirUsuario, idFolder)).setSpaces("drive")
					.setFields("nextPageToken, files(id, name, parents)").setPageToken(pageToken).execute();
			if (result.getFiles() != null && result.getFiles().size() == 1) {
				// encontrou diret�rio usu�rio
				diretorioUsuario = result.getFiles().get(0);
				break;
			}
			pageToken = result.getNextPageToken();
		} while (pageToken != null);

		if (diretorioUsuario == null) {
			// n�o encontrou diret�rio usu�rio, ir� cri�-lo
			File fileMetadata = new File();
			fileMetadata.setName(idDirUsuario);
			fileMetadata.setMimeType("application/vnd.google-apps.folder");
			fileMetadata.setParents(Collections.singletonList(idFolder));
			diretorioUsuario = service.files().create(fileMetadata).setFields("id").execute();
		}
		return diretorioUsuario;
	}

	protected String getUrlBase(EnumSistema sistema) {
		String[] profiles = env.getActiveProfiles();

		boolean isProducao = false;
		if (profiles.length > 0 && "prod".contentEquals(profiles[0])) {
			isProducao = true;
		}
		return isProducao ? sistema.getUrlBase() : URL_BASE_LOCALHOST + env.getProperty("server.port");
	}

	private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		InputStream in = GoogleDriveFachada.class.getResourceAsStream(credentialsFile);
		if (in == null) {
			LOGGER.error("GoogleDriveFachada n�o iniciado. Resource not found: " + credentialsFile);
		} else {
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
					.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).build();
			return flow.loadCredential("user");
		}
		return null;
	}

}