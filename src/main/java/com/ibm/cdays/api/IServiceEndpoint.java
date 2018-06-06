package com.ibm.cdays.api;

import akka.http.javadsl.server.Route;

/**
 * Interface for web-resources used by {@link ApiControllerFactory}.
 *
 * @author Michael Wellner (michael.wellner@de.ibm.com).
 */
public interface IServiceEndpoint {

    /**
     * Creates an Akka HTTP {@link Route} object for the resource.
     *
     * @return The {@link Route}
     */
    Route create();

}
