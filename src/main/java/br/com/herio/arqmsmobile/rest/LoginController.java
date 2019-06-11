package br.com.herio.arqmsmobile.rest;

import java.net.URI;

import br.com.herio.arqmsmobile.dto.DtoAutenticacao;
import br.com.herio.arqmsmobile.infra.security.TokenAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Api("LoginController")
@RestController
@RequestMapping("/public/login")
public class LoginController {
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	@Value("${server.port}")
	private String SERVER_PORT;

	@ApiOperation("fazerLogin")
	@PostMapping
	public ResponseEntity<?> fazerLogin(@RequestBody DtoAutenticacao dtoAutenticacao) {

		RequestEntity<DtoAutenticacao> request = RequestEntity
				.post(URI.create("http://localhost:" + SERVER_PORT + "/login"))
				.contentType(MediaType.APPLICATION_JSON).body(dtoAutenticacao);
		try {
			ResponseEntity<String> response = restTemplateBuilder.build().exchange(request, String.class);
			return ResponseEntity.ok(response.getHeaders().get(TokenAuthenticationService.AUTH_HEADER_NAME).get(0));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}
}
