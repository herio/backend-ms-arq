package br.com.herio.arqmsmobile.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dto.EnumSistema;
import br.com.herio.arqmsmobile.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("FotosController")
@RestController
public class FotosController {
	@Autowired
	private UsuarioService usuarioService;

	@ApiOperation("uploadFoto")
	@PostMapping("/files/{sistema}/usuarios/{idUsuario}/fotos")
	public Usuario uploadFoto(@PathVariable EnumSistema sistema, @PathVariable Long idUsuario,
			@RequestParam("foto") MultipartFile file) {
		return usuarioService.uploadFoto(idUsuario, sistema, file);
	}

	@ApiOperation("downloadFoto")
	@GetMapping("/publico/files/{sistema}/usuarios/{idUsuario}/fotos/{idFoto}")
	public ResponseEntity<Resource> downloadFoto(@PathVariable EnumSistema sistema, @PathVariable Long idUsuario,
			@PathVariable String idFoto, HttpServletRequest request) throws FileNotFoundException {
		File file = usuarioService.downloadFoto(idFoto, "foto.jpg");

		// Try to determine file's content type
		String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
		Resource resource = new InputStreamResource(new FileInputStream(file));
		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
				.body(resource);
	}

}
