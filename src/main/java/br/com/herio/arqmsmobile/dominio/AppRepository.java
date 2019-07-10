package br.com.herio.arqmsmobile.dominio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends CrudRepository<App, Long> {

	List<App> findAll();
}
