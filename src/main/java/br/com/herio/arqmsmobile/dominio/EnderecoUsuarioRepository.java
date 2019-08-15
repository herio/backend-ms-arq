package br.com.herio.arqmsmobile.dominio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoUsuarioRepository extends CrudRepository<EnderecoUsuario, Long> {

	List<EnderecoUsuario> findAllByUsuarioId(Long usuarioId);

}
