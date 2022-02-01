package br.com.herio.arqmsmobile.dominio;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogNotificacaoRepository extends CrudRepository<LogNotificacao, Long> {

	@Query(nativeQuery = true, value = "SELECT a.* FROM {h-schema}log_notificacao a WHERE to_char(a.data_criacao,'YYYY-MM-DD') = :dataCriacao ")
	List<LogNotificacao> findByData(@Param("dataCriacao") String dataCriacao);

	List<LogNotificacao> findAll();

	LogNotificacao findTopByOrderByIdDesc();

	@Transactional
	Long deleteAllByDataCriacaoBefore(LocalDateTime dataLimite);

}
