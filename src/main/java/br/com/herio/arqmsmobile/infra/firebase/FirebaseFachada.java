package br.com.herio.arqmsmobile.infra.firebase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import br.com.herio.arqmsmobile.dominio.Notificacao;

@Component
public class FirebaseFachada {
	// https://firebase.google.com/docs/admin/setup#initialize_the_sdk
	// https://firebase.google.com/docs/cloud-messaging/send-message#send_messages_to_specific_devices
	// https://github.com/firebase/firebase-admin-java/blob/d8c611153cd92a6cf872b232667d65977113a13e/
	// src/test/java/com/google/firebase/snippets/FirebaseMessagingSnippets.java

	private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseFachada.class);

	@Value("${firebase.credentialsFile}")
	private String credentialsFile;

	@Value("${firebase.credentialsFilePago}")
	private String credentialsFilePago;

	@Value("${firebase.urlDatabase}")
	private String urlDatabase;

	@Value("${firebase.urlDatabasePago}")
	private String urlDatabasePago;

	private FirebaseApp gratis;
	private FirebaseApp pago;

	@PostConstruct
	public void init() {
		LOGGER.info(String.format("FirebaseFachada init credentialsFile[%s], urlDatabase[%s]", credentialsFile, urlDatabase));
		try {
			if (credentialsFile != null && !"".equals(credentialsFile)) {
				InputStream in = FirebaseFachada.class.getResourceAsStream(credentialsFile);
				if (in == null) {
					throw new FileNotFoundException("Resource not found: " + credentialsFile);
				}
				FirebaseOptions.Builder optionsBuilder = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(in));
				if(urlDatabase != null && !"".equals(urlDatabase)) {
					optionsBuilder.setDatabaseUrl(urlDatabase); 
				}
				FirebaseOptions options= optionsBuilder.build();
				gratis = FirebaseApp.initializeApp(options, "gratis");
			} else {
				LOGGER.error("FirebaseFachada não iniciado. credentialsFile nulo", credentialsFile, urlDatabase);
			}

			if (credentialsFilePago != null && !"".equals(credentialsFilePago) && urlDatabasePago != null && !"".equals(urlDatabasePago)) {
				InputStream in = FirebaseFachada.class.getResourceAsStream(credentialsFilePago);
				if (in == null) {
					throw new FileNotFoundException("Resource not found: " + credentialsFilePago);
				}
				FirebaseOptions.Builder optionsBuilder = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(in));
				if(urlDatabase != null && !"".equals(urlDatabase)) {
					optionsBuilder.setDatabaseUrl(urlDatabasePago);
				}						
				FirebaseOptions options = optionsBuilder.build();
				pago = FirebaseApp.initializeApp(options, "pago");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean enviaNotificacao(Notificacao notificacao, boolean versaoPaga) {
		try {
			Message message = Message.builder()
					.setNotification(new Notification(notificacao.getTitulo(), notificacao.getConteudo()))
					.putAllData(notificacao.getMapDadosExtras())
					.setToken(notificacao.getDispositivo().getNumRegistro().replaceAll("naoautenticado", ""))
					.build();
			FirebaseApp app = versaoPaga ? pago : gratis;
			String response = FirebaseMessaging.getInstance(app).send(message);
			LOGGER.debug("FirebaseFachada enviaNotificacao", response);
			return response != null;
		} catch (FirebaseMessagingException e) {
			LOGGER.error("FirebaseFachada enviaNotificacao erro", e);
			throw new RuntimeException(e);
		}
	}

}
