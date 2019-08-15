package br.com.herio.arqmsmobile.dominio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoUsuarioRepository extends CrudRepository<ArquivoUsuario, Long> {

}
