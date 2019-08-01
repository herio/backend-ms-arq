package br.com.herio.arqmsmobile.dominio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtivacaoUsuarioRepository extends CrudRepository<AtivacaoUsuario, Long> {

	Optional<AtivacaoUsuario> findByUsuarioId(Long usuarioId);

	Optional<AtivacaoUsuario> findByUsuarioIdAndChaveAtivacaoAndDataAtivacaoIsNull(Long usuarioId, String chaveAtivacao);
}
