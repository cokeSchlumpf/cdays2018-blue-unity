package com.ibm.cdays.api.about;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import com.ibm.cdays.api.IServiceEndpoint;
import com.ibm.cdays.common.JsonUtil;
import com.ibm.cdays.configuration.ApplicationConfiguration;

public class AboutService extends AllDirectives implements IServiceEndpoint {

    @Override
    public Route create() {
        final About about = new About(ApplicationConfiguration.cdays.name, ApplicationConfiguration.cdays.build);
        return route(complete(StatusCodes.OK, about, Jackson.marshaller(JsonUtil.JSON_MAPPER_PRETTY)));
    }

}
