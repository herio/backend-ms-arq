package br.com.herio.arqmsmobile.infra.excecao;

public class ExcecaoNegocio extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExcecaoNegocio(String message) {
		super(message);
	}

}
