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

	private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseFachada.class);

	@Value("${firebase.credentialsFile}")
	private String credentialsFile;

	@Value("${firebase.urlDatabase}")
	private String urlDatabase;

	@PostConstruct
	public void init() {
		LOGGER.debug(String.format("FirebaseFachada init credentialsFile[%s], urlDatabase[%s]", credentialsFile, urlDatabase));
		try {
			if (credentialsFile != null && !"".equals(credentialsFile) && urlDatabase != null && !"".equals(urlDatabase)) {
				InputStream in = FirebaseFachada.class.getResourceAsStream(credentialsFile);// noticias-juridicas-45015-firebase-adminsdk-lrh3u-bd08f09ccd.json
				if (in == null) {
					throw new FileNotFoundException("Resource not found: " + credentialsFile);
				}
				FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(in))
						.setDatabaseUrl(urlDatabase) // "https://noticias-juridicas-45015.firebaseio.com"
						.build();
				FirebaseApp.initializeApp(options);
			} else {
				LOGGER.debug("FirebaseFachada não iniciado. credentialsFile, urlDatabase nulos ", credentialsFile, urlDatabase);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean enviaNotificacao(Notificacao notificacao) {
		try {
			Message message = Message.builder()
					.setNotification(new Notification(notificacao.getTitulo(), notificacao.getConteudo()))
					.putAllData(notificacao.getMapDadosExtras())
					.setToken(notificacao.getDispositivo().getNumRegistro())
					.build();
			String response = FirebaseMessaging.getInstance().send(message);
			LOGGER.debug("FirebaseFachada enviaNotificacao", response);
			return response != null;
		} catch (FirebaseMessagingException e) {
			LOGGER.error("FirebaseFachada enviaNotificacao erro", e);
			throw new RuntimeException(e);
		}
	}

}
