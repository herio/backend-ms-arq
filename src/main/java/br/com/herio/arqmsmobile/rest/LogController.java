package br.com.herio.arqmsmobile.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api("LogController")
@RestController
@RequestMapping("/actuator/log")
public class LogController {
	private static final String NO_CACHE = "no-cache";
	public static final int QTD_LINHAS = 200;

	@Value("${logging.file}")
	private String log;

	@GetMapping("/tail")
	public void tail(HttpServletResponse response) {
		File f = new File(log);
		try {
			ReversedLinesFileReader r = new ReversedLinesFileReader(f, Charset.defaultCharset());
			for (Integer i = 0; i < QTD_LINHAS; i++) {
				String line = r.readLine();
				if (line == null) {
					break;
				}
				response.getWriter().append(line).append(System.getProperty("line.separator"));
			}
			response.flushBuffer();
			r.close();
		} catch (IOException e) {
			throw new RuntimeException("Erro em tail", e);
		} finally {
		}
	}

	@GetMapping
	public void log(HttpServletResponse response) {
		try {
			InputStream is = new FileInputStream(log);
			response.setContentType("application/x-download");
			response.setHeader("Pragma", NO_CACHE);
			response.setHeader("Cache-Control", NO_CACHE);
			response.addHeader("Content-Disposition",
					"attachment; filename=\"app.log\"");
			response.getOutputStream().write(IOUtils.toByteArray(is));
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Erro em log", e);
		}
	}
}
