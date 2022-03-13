package br.com.herio.arqmsmobile.dominio;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends CrudRepository<Notificacao, Long> {

	Page<Notificacao> findAllByEnviadaAndExcluidaAndNotificacaoOrigemIsNullAndDispositivoUsuarioIdOrderByDataCriacaoDesc(boolean enviada,
			boolean excluida, Long usuarioId, Pageable page);

	Page<Notificacao> findAllByNotificacaoOrigemIsNullAndDispositivoUsuarioIdOrderByDataCriacaoDesc(Long usuarioId, Pageable page);

	Optional<Notificacao> findTopByEnviadaAndDispositivoUsuarioIdOrderByDataCriacaoDesc(boolean enviada, Long usuarioId);

	Optional<Notificacao> findTopByEnviadaAndDispositivoUsuarioIdAndDadosExtrasIgnoreCaseContainingOrderByDataCriacaoDesc(boolean enviada,
			Long usuarioId, String dadosExtras);

	Optional<Notificacao> findTopByEnviadaAndDispositivoUsuarioIdAndDadosExtrasIgnoreCaseContainingAndDataEnvioBetweenOrderByDataCriacaoDesc(
			boolean enviada, Long usuarioId, String dadosExtras, LocalDateTime dataInicio, LocalDateTime dataFim);

	Optional<Notificacao> findTopByEnviadaAndDadosExtrasIgnoreCaseContainingOrderByDataCriacaoDesc(boolean enviada, String dadosExtras);

	Page<Notificacao> findAllByEnviadaAndExcluidaAndNotificacaoOrigemIsNullAndDispositivoUsuarioIdAndDadosExtrasIgnoreCaseContainingOrderByDataCriacaoDesc(
			boolean enviada, boolean excluida, Long usuarioId, String dadosExtras, Pageable page);

	@Query(value = "FROM Notificacao n WHERE n.enviada = :enviada AND n.excluida = :excluida AND n.notificacaoOrigem is NULL"
			+ " AND n.dadosExtras like %:dadosExtras% AND n.usuarioOrigem.id = :usuarioId ORDER BY n.dataCriacao DESC")
	Page<Notificacao> findAllEnvidasNaoExcluidasDadosExtras(@Param("enviada") boolean enviada, @Param("excluida") boolean excluida,
			@Param("usuarioId") Long usuarioId, @Param("dadosExtras") String dadosExtras, Pageable page);

	@Transactional
	Long deleteAllByDataCriacaoBefore(LocalDateTime dataHoraLimite);
}
