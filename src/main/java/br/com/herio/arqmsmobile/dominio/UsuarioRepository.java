package br.com.herio.arqmsmobile.dominio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Optional<Usuario> findByEmailAndSistema(String email, String sistema);

	Optional<Usuario> findByCelularAndSistema(String celular, String sistema);

	Optional<Usuario> findByLoginIgnoreCaseAndSistema(String login, String sistema);

	List<Usuario> findAllByNomeNotNullAndEmailNotNullOrderByNomeAsc();

	List<Usuario> findAllByAdmin(boolean admin);

	List<Usuario> findAllByNomeContainingIgnoreCaseOrderByIdDesc(String nome);

	List<Usuario> findAllByEmailContainingIgnoreCaseOrderByIdDesc(String email);

	List<Usuario> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByIdDesc(String nome, String email);
}
