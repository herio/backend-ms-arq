package br.com.herio.arqmsmobile.dominio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoUsuarioRepository extends CrudRepository<ArquivoUsuario, Long> {

	Optional<ArquivoUsuario> findByIdDrive(String idDrive);
}
