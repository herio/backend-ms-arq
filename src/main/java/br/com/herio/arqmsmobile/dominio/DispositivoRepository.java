package br.com.herio.arqmsmobile.dominio;

import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.dto.EnumTipoSO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface DispositivoRepository extends CrudRepository<Dispositivo, Long> {

	@Query("SELECT a FROM Dispositivo a WHERE a.usuario.id = :idUsuario AND a.sistema = :sistema AND a.dataExclusao IS NULL")
	List<Dispositivo> findByIdUsuarioSistemaAtivos(@Param("idUsuario") Long idUsuario, @Param("sistema") EnumSistema sistema);

	@Query("SELECT a FROM Dispositivo a WHERE a.usuario.id = :idUsuario AND a.sistema = :sistema")
	List<Dispositivo> findByIdUsuarioSistema(@Param("idUsuario") Long idUsuario, @Param("sistema") EnumSistema sistema);

	@Query("SELECT a FROM Dispositivo a WHERE a.usuario.id = :idUsuario AND a.numRegistro = :numRegistro AND a.os = :os AND a.sistema = :sistema")
	List<Dispositivo> findByIdUsuarioNumRegistroOsSistema(@Param("idUsuario") Long idUsuario, @Param("numRegistro") String numRegistro, @Param("os") EnumTipoSO os, @Param("sistema") EnumSistema sistema);
}
