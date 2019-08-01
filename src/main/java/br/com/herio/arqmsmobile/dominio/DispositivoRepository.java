package br.com.herio.arqmsmobile.dominio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.herio.arqmsmobile.dto.EnumTipoSO;

@Repository
public interface DispositivoRepository extends CrudRepository<Dispositivo, Long> {

	List<Dispositivo> findAllByUsuarioIdAndDataExclusaoIsNull(Long usuarioId);

	List<Dispositivo> findAllByUsuarioId(Long usuarioId);

	Optional<Dispositivo> findByNumRegistroAndSo(String numRegistro, EnumTipoSO so);
}
