package ch.mobi.pyt;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

/**
 * This class defines the routes for our API server.
 *
 * @author michael.wellner@mobi.ch
 */
public final class Routes extends AllDirectives {

    private Route liveness() {
        return complete("Ok");
    }

    private Route readiness() {
        return complete("Ok");
    }

    private Route status_health() {
        return complete("Ok");
    }

    /**
     * Returns the routes for the API server.
     *
     * @return The Akka HTTP routes
     */
    public Route createRoute() {
        return route(
            path("liveness", () -> get(this::liveness)),
            path("readiness", () -> get(this::readiness)),
            pathPrefix("status", () ->
                path("health", () -> get(this::status_health)))
        );
    }

}
