package br.com.herio.arqmsmobile.dominio;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracaoNotificacaoRepository extends CrudRepository<ConfiguracaoNotificacao, Long> {

	@Query("SELECT a FROM ConfiguracaoNotificacao a WHERE a.usuario.id = :idUsuario")
	Optional<ConfiguracaoNotificacao> findByUsuarioId(Long idUsuario);
}
