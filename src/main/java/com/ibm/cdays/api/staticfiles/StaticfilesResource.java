package com.ibm.cdays.api.staticfiles;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import com.ibm.cdays.api.IServiceEndpoint;

/**
 * @author Michael Wellner (michael.wellner@mobi.ch).
 */
public class StaticfilesResource extends AllDirectives implements IServiceEndpoint {

    private static final String WEB_RESOURCE_DIRECTORY = "webapp";

    @Override
    public Route create() {
        return route(path(PathMatchers.remaining(), this::serveFiles));
    }

    private Route serveFiles(String path) {
        if (path.isEmpty()) {
            return serveFiles("index.html");
        } else {
            return getFromResource(String.format("%s/%s", WEB_RESOURCE_DIRECTORY, path));
        }
    }

}
