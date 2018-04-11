package ch.mobi.pyt;

import org.junit.*;

import static io.restassured.RestAssured.get;

/**
 * Sample Tests for the {@link Controller}.
 *
 * @author michael.wellner@mobi.ch
 */
public class ControllerITest {

    private Controller controller;

    @After
    public void after() {
        if (controller != null) {
            controller.stop();
        }
    }

    @Before
    public void before() {
        controller = Controller.create();
    }

    @Test
    public void liveness_resource_returns_200() {
        get(String.format("http://%s:%d/liveness", controller.hostname, controller.port))
            .then()
            .assertThat()
            .statusCode(200);
    }

    @Test
    public void readiness_resource_returns_200() {
        get(String.format("http://%s:%d/readiness", controller.hostname, controller.port))
            .then()
            .assertThat()
            .statusCode(200);
    }

    @Test
    public void status_health_resource_returns_200() {
        get(String.format("http://%s:%d/readiness", controller.hostname, controller.port))
            .then()
            .assertThat()
            .statusCode(200);
    }

}
