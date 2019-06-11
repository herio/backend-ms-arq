package br.com.herio.arqmsmobile.infra.feign;

import br.com.herio.arqmsmobile.infra.excecao.ExcecaoNegocio;
import br.com.herio.arqmsmobile.infra.excecao.dto.DtoExcecaoArqref8;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeignErrorDecoder.class);
    private ErrorDecoder delegate = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String json = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
            int status = response.status();
            LOGGER.error(String.format("status[%s] json[%s]", status, json));

            Exception exception = trataExcecaoNegocio(json, status);
            if(exception != null) {
                return exception;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return delegate.decode(methodKey, response);
    }

    private Exception trataExcecaoNegocio(String json, int status) {
        try {
            String mensagem = null;
			if(status == HttpStatus.SC_BAD_REQUEST && json.contains("url")) {
                // excecoes arqref8 sao lancadas com status 400
                DtoExcecaoArqref8 excecaoRESTArqRef8 = new ObjectMapper().readValue(json, DtoExcecaoArqref8.class);
                mensagem = excecaoRESTArqRef8.getMensagem();
            }
            return mensagem == null ? null : new ExcecaoNegocio(mensagem);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
