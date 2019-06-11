package br.com.herio.arqmsmobile.dto

class DtoNotificacao {
    String titulo;
    String conteudo;
    Long id;
    Long idProcesso;
    Long idDocumento;
    DtoDispositivo dispositivo;
    String dataEnvio;
    Boolean pendenteVisualizacao;
    Boolean notificacaoEnviada;
    Boolean excluida;
}

