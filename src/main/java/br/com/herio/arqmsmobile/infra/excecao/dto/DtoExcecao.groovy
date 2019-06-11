package br.com.herio.arqmsmobile.infra.excecao.dto

class DtoExcecao {
    String mensagem;
    String causa;

    DtoExcecao() {

    }

    DtoExcecao(String messagem, String causa) {
        this.mensagem = messagem;
        this.causa = causa;
    }
}
