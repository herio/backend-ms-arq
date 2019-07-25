package br.com.herio.arqmsmobile.dominio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends CrudRepository<Notificacao, Long> {

}
