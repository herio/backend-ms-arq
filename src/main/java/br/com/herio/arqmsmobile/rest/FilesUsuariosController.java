package br.com.herio.arqmsmobile.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.herio.arqmsmobile.dominio.ArquivoUsuario;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("FilesUsuariosController")
@RestController
public class FilesUsuariosController {
	@Autowired
	private UsuarioService usuarioService;

	// FOTOS
	@ApiOperation("uploadFoto")
	@PostMapping("/usuarios/{idUsuario}/files/fotos")
	public Usuario uploadFoto(@PathVariable Long idUsuario,
			@RequestParam("foto") MultipartFile file) {
		return usuarioService.uploadFoto(idUsuario, file);
	}

	@ApiOperation("downloadFoto")
	@GetMapping("/publico/usuarios/{idUsuario}/files/fotos")
	public ResponseEntity<Resource> downloadFoto(@PathVariable Long idUsuario,
			@RequestParam(required = false) boolean thumb, HttpServletRequest request) throws FileNotFoundException {
		File file = usuarioService.downloadFoto(idUsuario, thumb);
		String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
		Resource resource = new InputStreamResource(new FileInputStream(file));
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
				.body(resource);
	}

	@ApiOperation("deleteFoto")
	@DeleteMapping("/usuarios/{idUsuario}/files/fotos")
	public boolean deleteFoto(@PathVariable Long idUsuario) {
		return usuarioService.deleteFoto(idUsuario);
	}

	// ARQUIVOS
	@ApiOperation("deleteArquivo")
	@DeleteMapping("/usuarios/{idUsuario}/files/arquivos/{idDrive}")
	public boolean deleteArquivo(@PathVariable Long idUsuario, @PathVariable String idDrive) {
		return usuarioService.deleteArquivo(idUsuario, idDrive);
	}

	@ApiOperation("recuperaArquivosComAtributos")
	@GetMapping("/usuarios/{idUsuario}/files/arquivos")
	public Collection<ArquivoUsuario> recuperaArquivosComAtributos(@PathVariable Long idUsuario,
			@RequestParam String atributos) {
		return usuarioService.recuperaArquivosComAtributos(idUsuario, atributos);
	}

	@ApiOperation("uploadArquivo")
	@PostMapping("/usuarios/{idUsuario}/files/arquivos")
	public ArquivoUsuario uploadArquivo(@PathVariable Long idUsuario, @RequestParam("arquivo") MultipartFile file,
			@RequestParam(required = false) String atributos) {
		return usuarioService.uploadArquivo(idUsuario, file, atributos);
	}

	@ApiOperation("downloadArquivo")
	@GetMapping("/publico/usuarios/{idUsuario}/files/arquivos/{idDrive}")
	public ResponseEntity<Resource> downloadArquivo(@PathVariable Long idUsuario,
			@PathVariable String idDrive, @RequestParam(required = false) boolean thumb,
			HttpServletRequest request) throws FileNotFoundException {
		File file = usuarioService.downloadArquivo(idDrive, thumb);
		String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
		Resource resource = new InputStreamResource(new FileInputStream(file));
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
				.body(resource);
	}

}
