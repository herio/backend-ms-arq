package br.com.herio.arqmsmobile.service;

import java.util.Base64;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dto.EnumSistema;

@Service
public class EnviadorEmailService {

	@Autowired
	protected JavaMailSender javaMailSender;

	public String enviaEmailRecuperaSenha(Usuario usuario, EnumSistema sistema) {
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = criaHelper(msg, usuario.getEmail(), String.format("App %s - Recuperação de senha", sistema.getNome()));

			String email = new StringBuilder()
				.append(addHeader(sistema.getIcone(), String.format("%s - Recuperação de senha", sistema.getNome())))
				.append("<br/><br/>Olá %s, <br/><br/>Sua senha descriptograda é: <b>%s</b>")
				.append(addFooter())
				.toString();
			helper.setText(String.format(email, usuario.getNome(), 
					new String(Base64.getDecoder().decode(usuario.getSenha()))), true);

			javaMailSender.send(msg);
			return "E-mail de recuperação de senha enviado com sucesso! Verifique sua caixa de e-mail.";
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar email", e);
		}
	}

	public void enviaEmailBoasVindas(Usuario usuario, EnumSistema sistema) {
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = criaHelper(msg, usuario.getEmail(), String.format("Seja bem vindo ao App %s", sistema.getNome()));

			String email = new StringBuilder()
				.append(addHeader(sistema.getIcone(), String.format("%s - Dados de Cadastro", sistema.getNome())))
				.append(addDadosUsuario(usuario, sistema))
				.append(addFooter())
				.toString();
			helper.setText(String.format(email, usuario.getNome(), usuario.getEmail(),
					new String(Base64.getDecoder().decode(usuario.getSenha()))), true);

			javaMailSender.send(msg);
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar email", e);
		}
	}
	
	public void enviaEmailAtualizacaoDados(Usuario usuario, EnumSistema sistema) {
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = criaHelper(msg, usuario.getEmail(), String.format("Seus dados foram atualizados no App %s", sistema.getNome()));

			String email = new StringBuilder()
				.append(addHeader(sistema.getIcone(), String.format("%s - Dados atualizados", sistema.getNome())))
				.append(addDadosUsuario(usuario, sistema))
				.append(addFooter())
				.toString();
			helper.setText(String.format(email, usuario.getNome(), usuario.getEmail(),
					new String(Base64.getDecoder().decode(usuario.getSenha()))), true);

			javaMailSender.send(msg);
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar email", e);
		}
	}

	private MimeMessageHelper criaHelper(MimeMessage msg, String destinatario, String assunto) {
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg);
			helper.setFrom("Juris Apps <contatojurisapps@gmail.com>");
			helper.setTo(destinatario);
			helper.setSubject(assunto);
			return helper;
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar email", e);
		}
	}

	private String addHeader(String urlIcone, String titulo) {
		return new StringBuilder("<table><tr>")
			.append(String.format("<td><img width='100px' height='100px' src='%s'/></td>", urlIcone))
			.append(String.format("<td><h1>%s</h1></td></tr></table>", titulo)).toString();
	}

	private String addDadosUsuario(Usuario usuario, EnumSistema sistema) {
		return new StringBuilder()
			.append("<table><tr>")
			.append(String.format("<td><img width='100px' height='100px' src='%s'/></td>",
					usuario.getUrlFoto() == null ? sistema.getDefaultAvatar() : usuario.getUrlFoto()))
			.append("<td><b>Nome:</b> %s")
			.append("<br/><b>E-mail:</b> %s")
			.append("<br/><b>Senha:</b> %s</td>")
			.append("</tr></table>").toString();
	}

	private String addFooter() {
		return new StringBuilder()
			.append("<br/><br/>Caso queira atualizar seus dados, entre no App e vá em: Configurações > Atualize seus dados.")
			.append("<br/><br/>Atenciosamente, Juris Apps.<br/><br/>")
			.toString();
	}
}
