package br.com.herio.arqmsmobile.infra.excecao;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.herio.arqmsmobile.infra.excecao.dto.DtoExcecao;

@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class TratadorExcecaoRestController {
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(ExcecaoSessaoInvalida.class)
	public DtoExcecao tratarExcecaoSessaoInvalida(ExcecaoSessaoInvalida e) {
		// ExcecaoSessaoInvalida deve retornar http status 401
		String causa = ExceptionUtils.getStackTrace(e);
		return new DtoExcecao(e.getMessage(), causa);

	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler(ExcecaoNegocio.class)
	public DtoExcecao tratarExcecaoNegocio(ExcecaoNegocio e) {
		// excecao negocio deve retornar http status 412
		String causa = ExceptionUtils.getStackTrace(e);
		return new DtoExcecao(e.getMessage(), causa);
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler(ConstraintViolationException.class)
	public DtoExcecao tratarExcecaoNegocio(ConstraintViolationException e) {
		// ConstraintViolationException deve retornar http status 412
		String mensagem = "Já existe registro cadastrado com os valores informados.";
		String causa = ExceptionUtils.getStackTrace(e);
		return new DtoExcecao(mensagem, causa);
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler(NoSuchElementException.class)
	public DtoExcecao tratarExcecaoNegocio(NoSuchElementException e) {
		// ConstraintViolationException deve retornar http status 412
		String mensagem = "Registro inexistente.";
		String causa = ExceptionUtils.getStackTrace(e);
		return new DtoExcecao(mensagem, causa);
	}
}
