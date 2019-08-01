package br.com.herio.arqmsmobile.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.herio.arqmsmobile.dominio.AtivacaoUsuario;
import br.com.herio.arqmsmobile.dominio.AtivacaoUsuarioRepository;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;

@Service
public class AtivacaoUsuarioService {

	private static final int UMA_HORA_EM_SEGUNDOS = 3600;

	private static final int PERIODO_ATIVACAO_EM_HORAS = 24;

	@Autowired
	protected AtivacaoUsuarioRepository ativacaoUsuarioRepository;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	public AtivacaoUsuario gerarAtivacaoUsuario(Long idUsuario) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		if (usuario.isAtivado()) {
			throw new ExcecaoNegocio("Usuário já está ativado!");
		}
		Optional<AtivacaoUsuario> ativacao = ativacaoUsuarioRepository.findByUsuarioId(idUsuario);
		AtivacaoUsuario ativacaoUsuario;
		if (ativacao.isPresent()) {
			ativacaoUsuario = ativacao.get();
		} else {
			ativacaoUsuario = new AtivacaoUsuario();
			ativacaoUsuario.setUsuario(usuario);
		}
		ativacaoUsuario.setDataAtivacao(null);
		ativacaoUsuario.geraChaveAtivacao();
		ativacaoUsuarioRepository.save(ativacaoUsuario);
		return ativacaoUsuario;
	}

	public void confirmarAtivacaoUsuario(Long idUsuario, String chave) {
		AtivacaoUsuario ativacaoUsuario = ativacaoUsuarioRepository.findByUsuarioIdAndChaveAtivacaoAndDataAtivacaoIsNull(idUsuario, chave)
				.get();
		if (isPeriodoAtivacaoValido(ativacaoUsuario)) {
			ativacaoUsuario.setDataAtivacao(new Date());
			ativacaoUsuarioRepository.save(ativacaoUsuario);
			Usuario usuario = usuarioRepository.findById(idUsuario).get();
			usuario.setAtivado(true);
			usuarioRepository.save(usuario);
		} else {
			throw new ExcecaoNegocio("Prazo de ativação expirado, gere uma nova chave de ativação.");
		}
	}

	private boolean isPeriodoAtivacaoValido(AtivacaoUsuario ativacaoUsuario) {
		Date dataAtual = new Date();
		Date dataCadastro = java.sql.Timestamp.valueOf(ativacaoUsuario.getDataCriacao());
		long seconds = (dataAtual.getTime() - dataCadastro.getTime()) / 1000;
		int hours = (int) (seconds / UMA_HORA_EM_SEGUNDOS);
		return hours <= PERIODO_ATIVACAO_EM_HORAS;
	}

}
