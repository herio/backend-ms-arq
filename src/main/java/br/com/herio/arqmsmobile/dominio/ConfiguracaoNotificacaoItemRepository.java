package br.com.herio.arqmsmobile.dominio;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoNotificacaoItemRepository extends CrudRepository<ConfiguracaoNotificacaoItem, Long> {

	@Query("SELECT a FROM ConfiguracaoNotificacaoItem a WHERE a.configuracao.usuario.id = :usuarioId")
    Optional<ConfiguracaoNotificacaoItem> findByConfiguracaoUsuarioId(@Param("usuarioId") Long usuarioId);
}
