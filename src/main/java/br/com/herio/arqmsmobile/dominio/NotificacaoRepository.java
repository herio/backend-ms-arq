package br.com.herio.arqmsmobile.dominio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificacaoRepository extends CrudRepository<Notificacao, Long> {

	@Query(value = "SELECT a FROM Notificacao a WHERE a.enviada = true AND a.excluida = false AND a.notificacaoOrigem is null AND a.dispositivo.usuario.id = :usuarioId ORDER BY a.dataCriacao DESC")
	Page<Notificacao> findAllByEnviadaNaoExcluidaDispositivoUsuarioIdOrderByDataCriacaoDesc(@Param("usuarioId") Long usuarioId, Pageable page);

	Optional<Notificacao> findTopByEnviadaAndExcluidaAndDispositivoUsuarioIdOrderByDataCriacaoDesc(boolean enviada, boolean excluida, Long usuarioId);
}
