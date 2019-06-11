package br.com.herio.arqmsmobile.dto

import br.com.herio.arqmsmobile.dominio.Dispositivo

class DtoNotificacao {
    String titulo;
    String conteudo;
    Long id;
    Long idProcesso;
    Long idDocumento;
    Dispositivo dispositivo;
    String dataEnvio;
    Boolean pendenteVisualizacao;
    Boolean notificacaoEnviada;
    Boolean excluida;
}

