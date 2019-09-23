package br.com.herio.arqmsmobile.dominio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Optional<Usuario> findByEmailAndSistema(String email, String sistema);

	Optional<Usuario> findByLoginAndSistema(String login, String sistema);

	List<Usuario> findAllByNomeNotNullAndEmailNotNullOrderByIdDesc();

	List<Usuario> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByIdDesc(String nome, String email);

}
