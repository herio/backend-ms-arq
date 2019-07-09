package br.com.herio.arqmsmobile.dominio;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ConfiguracaoNotificacaoRepository extends CrudRepository<ConfiguracaoNotificacao, Long> {

    @Query("SELECT a FROM ConfiguracaoNotificacao a WHERE a.usuario.id = :idUsuario")
    List<ConfiguracaoNotificacao> findAllByUsuarioId(Long idUsuario);
}
