package br.com.herio.arqmsmobile.infra.excecao;

public class ExcecaoSessaoInvalida extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExcecaoSessaoInvalida() {
		super("Sessao invalida");
	}
}
