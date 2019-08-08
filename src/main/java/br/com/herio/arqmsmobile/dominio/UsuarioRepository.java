package br.com.herio.arqmsmobile.dominio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Optional<Usuario> findByLoginAndSistema(String login, String sistema);

	Optional<Usuario> findByLoginAndSenhaAndAtivadoAndSistema(String login, String senha, boolean ativado, String sistema);

}
