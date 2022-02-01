package br.com.herio.arqmsmobile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.herio.arqmsmobile.service.CleanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("CleanController")
@RestController
@RequestMapping("/actuator/clean")
public class CleanController {

	@Autowired
	protected CleanService cleanService;

	@ApiOperation("clean")
	@PostMapping
	public String clean() {
		return cleanService.clean();
	}

}
