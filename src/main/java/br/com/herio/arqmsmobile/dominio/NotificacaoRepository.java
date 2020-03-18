package br.com.herio.arqmsmobile.dominio;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends CrudRepository<Notificacao, Long> {

	Page<Notificacao> findAllByEnviadaAndExcluidaAndNotificacaoOrigemIsNullAndDispositivoUsuarioIdOrderByDataCriacaoDesc(
			boolean enviada, boolean excluida, Long usuarioId, Pageable page);

	Page<Notificacao> findAllByEnviadaAndExcluidaAndNotificacaoOrigemIsNullAndDispositivoUsuarioIdAndDadosExtrasIgnoreCaseContainingOrderByDataCriacaoDesc(
			boolean enviada, boolean excluida, Long usuarioId, String dadosExtras, Pageable page);

	Page<Notificacao> findAllByNotificacaoOrigemIsNullAndDispositivoUsuarioIdOrderByDataCriacaoDesc(Long usuarioId, Pageable page);

	Optional<Notificacao> findTopByEnviadaAndDispositivoUsuarioIdAndDadosExtrasIgnoreCaseContainingOrderByDataCriacaoDesc(boolean enviada,
			Long usuarioId, String dadosExtras);
}
