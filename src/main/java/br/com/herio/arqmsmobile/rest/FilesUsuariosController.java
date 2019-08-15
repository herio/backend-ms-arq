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

import br.com.herio.arqmsmobile.dominio.ArquivoUsuario;
import br.com.herio.arqmsmobile.dominio.EnumTipoArquivo;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("FilesUsuariosController")
@RestController
public class FilesUsuariosController {
	@Autowired
	private UsuarioService usuarioService;

	@ApiOperation("uploadFoto")
	@PostMapping("/files/usuarios/{idUsuario}/fotos")
	public Usuario uploadFoto(@PathVariable Long idUsuario,
			@RequestParam("foto") MultipartFile file) {
		return usuarioService.uploadFoto(idUsuario, file);
	}

	@ApiOperation("downloadFoto")
	@GetMapping("/publico/files/usuarios/{idUsuario}/fotos/{idFoto}")
	public ResponseEntity<Resource> downloadFoto(@PathVariable Long idUsuario,
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

	@ApiOperation("uploadArquivo")
	@PostMapping("/files/usuarios/{idUsuario}/arquivos")
	public ArquivoUsuario uploadArquivo(@PathVariable Long idUsuario, @RequestParam EnumTipoArquivo tipoArquivo,
			@RequestParam("arquivo") MultipartFile file) {
		return usuarioService.uploadArquivo(idUsuario, tipoArquivo, file);
	}

	@ApiOperation("downloadArquivo")
	@GetMapping("/files/usuarios/{idUsuario}/arquivos/{idArquivo}")
	public ResponseEntity<Resource> downloadArquivo(@PathVariable Long idUsuario,
			@PathVariable Long idArquivo, HttpServletRequest request) throws FileNotFoundException {
		File file = usuarioService.downloadArquivo(idArquivo);

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
