package br.com.herio.arqmsmobile.infra.excecao.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class DtoExcecaoArqref8 {
    String url;
    int statusCode;
    String mensagem;
}

