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
			MimeMessageHelper helper = new MimeMessageHelper(msg);
			helper.setFrom("Juris Apps <contatojurisapps@gmail.com>");
			helper.setTo(usuario.getEmail());
			helper.setSubject(String.format("App %s - Recuperação de senha", sistema.getNome()));

			String email = new StringBuilder("<table><tr>")
					.append(String.format("<td><img width='100px' height='100px' src='%s'/></td>", sistema.getIcone()))
					.append(String.format("<td><h1>%s - Recuperação de senha</h1></td></tr></table>", sistema.getNome()))
					.append("<br/><br/>Olá %s, <br/><br/>Sua senha descriptograda é: <b>%s</b>")
					.append("<br/><br/>Caso queira trocá-la, entre no App e vá em: Configurações > Atualize seus dados.")
					.append("<br/><br/>Atenciosamente, Juris Apps.<br/><br/>").toString();
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
			MimeMessageHelper helper = new MimeMessageHelper(msg);
			helper.setFrom("Juris Apps <contatojurisapps@gmail.com>");
			helper.setTo(usuario.getEmail());
			helper.setSubject(String.format("Seja bem vindo ao App %s", sistema.getNome()));

			String email = new StringBuilder("<table><tr>")
					.append(String.format("<td><img width='100px' height='100px' src='%s'/></td>", sistema.getIcone()))
					.append(String.format("<td><h1>%s - Dados de Cadastro</h1></td></tr></table>", sistema.getNome()))
					.append("<table><tr>")
					.append(String.format("<td><img width='100px' height='100px' src='%s'/></td>", sistema.getDefaultAvatar()))
					.append("<td><b>Nome:</b> %s")
					.append("<br/><b>E-mail:</b> %s")
					.append("<br/><b>Senha:</b> %s</td>")
					.append("</tr></table>")
					.append("<br/><br/>Caso queira atualizar seus dados, entre no App e vá em: Configurações > Atualize seus dados.")
					.append("<br/><br/>Atenciosamente, Juris Apps.<br/><br/>").toString();
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
			MimeMessageHelper helper = new MimeMessageHelper(msg);
			helper.setFrom("Juris Apps <contatojurisapps@gmail.com>");
			helper.setTo(usuario.getEmail());
			helper.setSubject(String.format("Seus dados foram atualizados no App %s", sistema.getNome()));

			String email = new StringBuilder("<table><tr>")
					.append(String.format("<td><img width='100px' height='100px' src='%s'/></td>", sistema.getIcone()))
					.append(String.format("<td><h1>%s - Dados atualizados</h1></td></tr></table>", sistema.getNome()))
					.append("<table><tr>")
					.append(String.format("<td><img width='100px' height='100px' src='%s'/></td>",
							usuario.getUrlFoto() == null ? sistema.getDefaultAvatar() : usuario.getUrlFoto()))
					.append("<td><b>Nome:</b> %s")
					.append("<br/><b>E-mail:</b> %s")
					.append("<br/><b>Senha:</b> %s</td>")
					.append("</tr></table>")
					.append("<br/><br/>Caso queira atualizar seus dados, entre no App e vá em: Configurações > Atualize seus dados.")
					.append("<br/><br/>Atenciosamente, Juris Apps.<br/><br/>").toString();
			helper.setText(String.format(email, usuario.getNome(), usuario.getEmail(),
					new String(Base64.getDecoder().decode(usuario.getSenha()))), true);

			javaMailSender.send(msg);
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar email", e);
		}
	}
}
