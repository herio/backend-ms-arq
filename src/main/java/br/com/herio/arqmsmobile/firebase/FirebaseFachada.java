package br.com.herio.arqmsmobile.firebase;

import br.com.herio.arqmsmobile.dto.EnumTipoSO;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class FirebaseFachada {
    //https://firebase.google.com/docs/admin/setup#initialize_the_sdk
    //https://firebase.google.com/docs/cloud-messaging/send-message#send_messages_to_specific_devices

    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseFachada.class);

    public boolean enviaNotificacao(String titulo, String conteudo, String destinatario, EnumTipoSO so) {
        try {
            Message message = Message.builder()
                    .setNotification(new Notification(titulo,conteudo))
                    .setToken(destinatario)
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            LOGGER.debug("FirebaseFachada enviaNotificacao", response);
        } catch (FirebaseMessagingException e) {
            LOGGER.error("FirebaseFachada enviaNotificacao erro", e);
            throw new RuntimeException(e);
        }
        return false;
    }

    public void init(String caminhoChave, String urlDatabase) {
        try {
            FileInputStream serviceAccount = new FileInputStream(caminhoChave);//noticias-juridicas-45015-firebase-adminsdk-lrh3u-bd08f09ccd.json
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(urlDatabase) //"https://noticias-juridicas-45015.firebaseio.com"
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
