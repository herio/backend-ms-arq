package br.com.herio.arqmsmobile.rest;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.dominio.LogNotificacao;
import br.com.herio.arqmsmobile.dominio.LogNotificacaoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("LogNotificacoesController")
@RestController
@RequestMapping("/lognotificacoes")
public class LogNotificacoesController {

	@Autowired
	protected LogNotificacaoRepository logNotificacaoRepository;

	@ApiOperation("listarLogsNotificacoes")
	@GetMapping
	public Collection<LogNotificacao> listarLogsNotificacoes() {
		return logNotificacaoRepository.findAll();
	}

	@ApiOperation("listarLogsNotificacoesPorData")
	@GetMapping("/{dataLog}")
	public Collection<LogNotificacao> listarLogsNotificacoesPorData(@PathVariable String dataLog) {
		// formato yyyy-MM-dd
		return logNotificacaoRepository.findByData(dataLog);
	}

	@ApiOperation("recuperarUltimoLogNotificacao")
	@GetMapping("/ultimo")
	public LogNotificacao recuperarUltimoLogNotificacao() {
		return logNotificacaoRepository.findTopByOrderByIdDesc();
	}

	@ApiOperation("recuperarPorId")
	@GetMapping("/{id}")
	public LogNotificacao recuperarPorId(@PathVariable Long id) {
		Optional<LogNotificacao> optLog = logNotificacaoRepository.findById(id);
		if (optLog.isPresent()) {
			return optLog.get();
		}
		return null;
	}

	@ApiOperation("removerLogNotificacao")
	@DeleteMapping("/{id}")
	public void removerApp(@PathVariable Long id) {
		logNotificacaoRepository.deleteById(id);
	}

}
