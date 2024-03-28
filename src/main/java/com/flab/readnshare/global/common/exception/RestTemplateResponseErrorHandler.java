package com.flab.readnshare.global.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is5xxServerError() ||
                response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new RestCallException(response.getStatusCode().toString(), response.getStatusText(), response.getStatusCode());
    }

    @Getter
    public static class RestCallException extends IOException {
        private final String code;
        private final String message;
        private final HttpStatusCode status;

        public RestCallException(String code, String message, HttpStatusCode status) {
            this.code = code;
            this.message = message;
            this.status = status;
        }
    }

}
