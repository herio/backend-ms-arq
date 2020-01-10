package br.com.herio.arqmsmobile.dominio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VigenciaUsuarioRepository extends CrudRepository<VigenciaUsuario, Long> {

}
