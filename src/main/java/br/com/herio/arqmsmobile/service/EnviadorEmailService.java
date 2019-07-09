package br.com.herio.arqmsmobile.service;

import java.io.UnsupportedEncodingException;
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
			MimeMessageHelper helper = criaHelper(msg, usuario.getEmail(), String.format("Recuperação de senha do App %s", sistema.getNome()));

			String email = new StringBuilder()
					.append(addHeader(sistema.getIcone(), String.format("Recuperação de senha do App %s", sistema.getNome())))
					.append(addDadosUsuario(usuario, sistema))
					.append(addFooter())
					.toString();
			helper.setText(email, true);

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
			helper.setText(email, true);

			javaMailSender.send(msg);
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar email", e);
		}
	}

	public void enviaEmailAtualizacaoDados(Usuario usuario, EnumSistema sistema) {
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = criaHelper(msg, usuario.getEmail(),
					String.format("Seus dados foram atualizados no App %s", sistema.getNome()));

			String email = new StringBuilder()
					.append(addHeader(sistema.getIcone(), String.format("%s - Dados atualizados", sistema.getNome())))
					.append(addDadosUsuario(usuario, sistema))
					.append(addFooter())
					.toString();
			helper.setText(email, true);

			javaMailSender.send(msg);
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar email", e);
		}
	}

	private MimeMessageHelper criaHelper(MimeMessage msg, String destinatario, String assunto) {
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, "ISO-8859-1");
			helper.setFrom("Juris Apps <contatojurisapps@gmail.com>");
			helper.setTo(destinatario);
			helper.setSubject(assunto);
			return helper;
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar email", e);
		}
	}

	private String addHeader(String urlIcone, String titulo) {
		return new StringBuilder("<html><head><meta charset='UTF-8'></head><table><tr>")
				.append(String.format("<td><img style='width:50px;height:50px' src='%s'/></td>", urlIcone))
				.append(String.format("<td><h2>%s</h2></td></tr></table>", titulo))
				.toString();
	}

	private String addDadosUsuario(Usuario usuario, EnumSistema sistema) {
		String img = usuario.getUrlFoto() == null ? sistema.getDefaultAvatar() : usuario.getUrlFoto();
		return new StringBuilder().append("<table><tr>")
				.append("<td><img style='width:100px;height:100px;border-radius: 50%' src='").append(img).append("'/></td>")
				.append("<td><b>Nome:</b> ").append(usuario.getNome())
				.append("<br/><b>E-mail:</b> ").append(usuario.getEmail())
				.append("<br/><b>Senha:</b> ").append(new String(Base64.getDecoder().decode(usuario.getSenha()))).append("</td>")
				.append("</tr></table>")
				.toString();
	}

	private String addFooter() {
		return new StringBuilder().append(
				"<br/><br/>Para atualizar seus dados, entre no App e vá em: Configurações > Atualize seus dados.")
				.append("<br/><br/>Atenciosamente, Juris Apps.<br/><br/></html>").toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String sistema = EnumSistema.NOTICIAS_JURIDICAS.getNome();
		byte[] latin1 = sistema.getBytes();
		byte[] utf8 = new String(latin1, "ISO-8859-1").getBytes("ISO-8859-1");
		byte[] latin11 = sistema.getBytes();
		byte[] utf81 = new String(latin11, "UTF-8").getBytes("UTF-8");
		byte[] latin111 = sistema.getBytes();
		byte[] utf811 = new String(latin111, "ISO-8859-1").getBytes("UTF-8");
		byte[] latin1111 = sistema.getBytes();
		byte[] utf8111 = new String(latin1111, "UTF-8").getBytes("ISO-8859-1");
		System.out.println(sistema);
		System.out.println(new String(utf8));
		System.out.println(new String(utf81));
		System.out.println(new String(utf811));
		System.out.println(new String(utf8111));
	}
}
