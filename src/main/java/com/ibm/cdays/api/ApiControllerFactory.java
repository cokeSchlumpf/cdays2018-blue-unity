package com.ibm.cdays.api;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.ibm.cdays.api.about.AboutService;
import com.ibm.cdays.api.staticfiles.StaticfilesResource;
import com.ibm.cdays.configuration.ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;

public class ApiControllerFactory extends AllDirectives {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    public ApiController create(ActorSystem system) {
        final String hostname = ApplicationConfiguration.cdays.api.hostname;
        final int port = ApplicationConfiguration.cdays.api.port;

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createRoutes().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
            ConnectHttp.toHost(hostname, port), materializer);

        LOG.info(String.format("Started API-Server on http://%s:%s", hostname, port));

        return new ApiController(hostname, port, binding);
    }

    private Route createRoutes() {
        final AboutService aboutService = new AboutService();
        final StaticfilesResource staticfilesResource = new StaticfilesResource();

        return route(
            pathPrefix("api", () ->
                pathPrefix("v1", () ->
                    route(
                        path("about", () -> get(aboutService::create))
                    )
                )
            ),
            staticfilesResource.create()
        );
    }

}
