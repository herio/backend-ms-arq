package br.com.herio.arqmsmobile.dominio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Optional<Usuario> findByLogin(String login);

	Optional<Usuario> findByLoginAndSenha(String login, String senha);

}
