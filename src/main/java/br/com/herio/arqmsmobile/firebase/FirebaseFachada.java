package br.com.herio.arqmsmobile.firebase;

import java.io.FileInputStream;
import java.io.IOException;

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

import javax.annotation.PostConstruct;

@Component
public class FirebaseFachada {
	// https://firebase.google.com/docs/admin/setup#initialize_the_sdk
	// https://firebase.google.com/docs/cloud-messaging/send-message#send_messages_to_specific_devices

	private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseFachada.class);

	@Value("${firebase.caminhoChave}")
	private String caminhoChave;

	@Value("${firebase.urlDatabase}")
	private String urlDatabase;

	public boolean enviaNotificacao(Notificacao notificacao) {
		try {
			Message message = Message.builder()
					.setNotification(new Notification(notificacao.getTitulo(), notificacao.getConteudo()))
					.setToken(notificacao.getDispositivo().getNumRegistro())
					.build();
			String response = FirebaseMessaging.getInstance().send(message);
			LOGGER.debug("FirebaseFachada enviaNotificacao", response);
		} catch (FirebaseMessagingException e) {
			LOGGER.error("FirebaseFachada enviaNotificacao erro", e);
			throw new RuntimeException(e);
		}
		return false;
	}

	@PostConstruct
	public void init() {
		try {
			FileInputStream serviceAccount = new FileInputStream(caminhoChave);// noticias-juridicas-45015-firebase-adminsdk-lrh3u-bd08f09ccd.json
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl(urlDatabase) // "https://noticias-juridicas-45015.firebaseio.com"
					.build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
