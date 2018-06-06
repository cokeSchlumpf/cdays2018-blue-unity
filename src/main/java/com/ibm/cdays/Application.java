package com.ibm.cdays;

import akka.actor.ActorSystem;
import akka.actor.dsl.Creators;
import com.ibm.cdays.api.ApiController;
import com.ibm.cdays.api.ApiControllerFactory;

/**
 * @author Michael Wellner (michael.wellner@de.ibm.com).
 */
public class Application {

    public final ActorSystem system;

    public final ApiController apiController;

    public static void main(String ...args) {
        Application.create();
    }

    private Application(ActorSystem system, ApiController apiController) {
        this.apiController = apiController;
        this.system = system;
    }

    public static Application create() {
        final ActorSystem system = ActorSystem.create("cdays2018");
        final ApiControllerFactory apiFactory = new ApiControllerFactory();
        final ApiController apiController = apiFactory.create(system);

        return new Application(system, apiController);
    }

}
