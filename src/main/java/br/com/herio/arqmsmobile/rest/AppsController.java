package br.com.herio.arqmsmobile.rest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.App;
import br.com.herio.arqmsmobile.dominio.AppRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("AppsController")
@RestController
public class AppsController {

	@Autowired
	protected AppRepository appRepository;

	@ApiOperation("listarApps")
	@GetMapping("/publico/apps")
	public Collection<App> listarApps() {
		return appRepository.findAll();
	}

	@ApiOperation("criarApp")
	@PostMapping("/apps")
	public App criarApp(@RequestBody App app) {
		return appRepository.save(app);
	}

	@ApiOperation("alterarApp")
	@PostMapping("/apps/{idApp}")
	public App alterarApp(@PathVariable Long idApp, @RequestBody App app) {
		App appBd = appRepository.findById(idApp).get();
		appBd.setTitulo(app.getTitulo());
		appBd.setDescricao(app.getDescricao());
		appBd.setAtivo(app.isAtivo());
		appBd.setUrlAndroid(app.getUrlAndroid());
		appBd.setUrlIos(app.getUrlIos());
		appBd.setUrlIcone(app.getUrlIcone());
		return appRepository.save(app);
	}

	@ApiOperation("removerApp")
	@DeleteMapping("/apps/{idApp}")
	public void removerApp(@PathVariable Long idApp) {
		appRepository.deleteById(idApp);
	}

}
