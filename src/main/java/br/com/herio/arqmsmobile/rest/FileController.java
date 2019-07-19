package br.com.herio.arqmsmobile.rest;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

@Api("FileController")
@RestController
public class FileController {
	@Autowired
	private UsuarioService usuarioService;

	@ApiOperation("uploadFoto")
	@PostMapping("/{sistema}/usuarios/{idUsuario}/foto")
	public Usuario uploadFoto(@PathVariable EnumSistema sistema, @PathVariable Long idUsuario,
			@RequestParam("foto") MultipartFile file) {
		return usuarioService.uploadFoto(idUsuario, sistema, file);
	}

	@ApiOperation("downloadFoto")
	@GetMapping("/publico/{sistema}/usuarios/{idUsuario}/foto/{idFoto}")
	public ResponseEntity<File> downloadFoto(@PathVariable EnumSistema sistema, @PathVariable Long idUsuario,
			@PathVariable String idFoto, HttpServletRequest request) {
		File file = usuarioService.downloadFoto(idFoto, "foto.jpg");

		// Try to determine file's content type
		String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
				.body(file);
	}

}
