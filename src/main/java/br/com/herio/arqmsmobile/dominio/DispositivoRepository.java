package br.com.herio.arqmsmobile.dominio;

import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.dto.EnumTipoSO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface DispositivoRepository extends CrudRepository<Dispositivo, Long> {

	@Query("SELECT a FROM Dispositivo a WHERE a.usuario.id = :idUsuario AND a.sistema = :sistema AND a.dataExclusao IS NULL")
	List<Dispositivo> findByIdUsuarioSistemaAtivos(Long idUsuario, EnumSistema sistema);

	@Query("SELECT a FROM Dispositivo a WHERE a.usuario.id = :idUsuario AND a.sistema = :sistema")
	List<Dispositivo> findByIdUsuarioSistema(Long idUsuario, EnumSistema sistema);

	@Query("SELECT a FROM Dispositivo a WHERE a.usuario.id = :idUsuario AND a.numRegistro = :numRegistro AND a.os = :os AND a.sistema = :sistema")
	List<Dispositivo> findByIdUsuarioNumRegistroOsSistema(Long idUsuario, String numRegistro, EnumTipoSO os, EnumSistema sistema);
}
