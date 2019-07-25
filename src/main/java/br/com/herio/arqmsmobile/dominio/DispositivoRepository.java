package br.com.herio.arqmsmobile.dominio;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.herio.arqmsmobile.dto.EnumTipoSO;

@Repository
public interface DispositivoRepository extends CrudRepository<Dispositivo, Long> {

	@Query("SELECT a FROM Dispositivo a WHERE a.usuario.id = :idUsuario AND a.dataExclusao IS NULL")
	List<Dispositivo> findByIdUsuarioAtivos(@Param("idUsuario") Long idUsuario);

	@Query("SELECT a FROM Dispositivo a WHERE a.usuario.id = :idUsuario")
	List<Dispositivo> findByIdUsuario(@Param("idUsuario") Long idUsuario);

	@Query("SELECT a FROM Dispositivo a WHERE a.usuario.id = :idUsuario AND a.numRegistro = :numRegistro AND a.so = :so")
	List<Dispositivo> findByIdUsuarioNumRegistroOs(@Param("idUsuario") Long idUsuario,
			@Param("numRegistro") String numRegistro, @Param("so") EnumTipoSO so);
}
