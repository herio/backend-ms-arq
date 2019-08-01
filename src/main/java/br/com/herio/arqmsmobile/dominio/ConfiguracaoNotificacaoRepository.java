package br.com.herio.arqmsmobile.dominio;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracaoNotificacaoRepository extends CrudRepository<ConfiguracaoNotificacao, Long> {

	Optional<ConfiguracaoNotificacao> findByUsuarioId(Long usuarioId);

	Collection<ConfiguracaoNotificacao> findAllByReceberNotificacao(boolean receberNotificacao);
}
