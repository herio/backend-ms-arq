package br.com.herio.arqmsmobile.dominio;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AtivacaoUsuarioRepository extends CrudRepository<AtivacaoUsuario, Long> {

	@Query("SELECT a FROM AtivacaoUsuario a WHERE a.usuario.id = :idUsuario")
	Optional<AtivacaoUsuario> findByUsuarioId(@Param("idUsuario") Long idUsuario);

	@Query("SELECT a FROM AtivacaoUsuario a WHERE a.usuario.id = :idUsuario AND a.chaveAtivacao = :chaveAtivacao AND a.dataAtivacao IS NULL")
	Optional<AtivacaoUsuario> findByUsuarioIdChaveAtivacaoValida(@Param("idUsuario") Long idUsuario, @Param("chaveAtivacao") String chaveAtivacao);
}
