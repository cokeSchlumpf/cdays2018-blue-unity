package com.ibm.cdays.api;

import akka.Done;
import akka.http.javadsl.ServerBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;

public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    public final String hostname;

    public final int port;

    public final CompletionStage<ServerBinding> serverBinding;

    public ApiController(String hostname, int port, CompletionStage<ServerBinding> serverBinding) {
        this.hostname = hostname;
        this.port = port;
        this.serverBinding = serverBinding;
    }

    public CompletionStage<Done> stop() {
        LOG.info(String.format("Stopping API-Server on http://%s:%s", hostname, port));

        return serverBinding
            .thenCompose(ServerBinding::unbind);
    }

}
