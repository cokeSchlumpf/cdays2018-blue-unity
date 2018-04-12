package ch.mobi.pyt;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.compat.java8.FutureConverters;

import java.io.Console;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

/**
 * The central main class of Pythagoras Controller
 *
 * @author michael.wellner@mobi.ch
 */
public class Controller {

    /**
     * The hostname of the API
     */
    public final String hostname;

    /**
     * The port of the API
     */
    public final int port;

    /**
     * The materializing ActorSystem
     */
    public final ActorSystem system;

    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);
    private final CompletionStage<ServerBinding> binding;

    /**
     * Private constructor to create an instance.
     *
     * @param binding The Akka HTTP binding
     */
    private Controller(ActorSystem system, String hostname, int port, CompletionStage<ServerBinding> binding) {
        this.binding = binding;
        this.hostname = hostname;
        this.port = port;
        this.system = system;
    }

    /**
     * Application starter.
     *
     * @param args We don't expect any arguments yet
     */
    public static void main(String... args) throws IOException {
        Controller controller = create();
        Console console = System.console();

        if (console != null) {
            System.out.println("");
            System.out.println("Press ENTER to stop the server");
            console.readLine();
            controller
                .stop()
                .thenAccept(done -> {
                    System.exit(0);
                });
        }
    }

    /**
     * Creates a new instance of the server by reading hostname and port from configuration.
     *
     * @return A new {@link Controller} instance
     */
    public static Controller create() {
        Config config = ConfigFactory.load();
        return create(config.getString("pythagoras.api.hostname"), config.getInt("pythagoras.api.port"));
    }

    /**
     * Stops the server.
     *
     * @return A future which resolves when the server is stopped.
     */
    public CompletionStage<Done> stop() {
        CompletionStage<Done> done = binding
            .thenCompose(ServerBinding::unbind)
            .thenComposeAsync(any -> FutureConverters.toJava(system.terminate()))
            .thenApply(any -> Done.getInstance());

        done.thenAccept(d -> LOG.info(String.format("Stopped API-Server on http://%s:%s", hostname, port)));

        return done;
    }

    /**
     * Creates a new instance of the server.
     *
     * @param hostname The hostname to which the server should be bound
     * @param port     The port to which the server should be bound
     * @return A new {@link Controller} instance
     */
    public static Controller create(String hostname, int port) {
        final ActorSystem system = ActorSystem.create("pyt-controller");

        final Routes routes = new Routes();
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = routes.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
            ConnectHttp.toHost(hostname, port), materializer);

        LOG.info(String.format("Started API-Server on http://%s:%s", hostname, port));

        return new Controller(system, hostname, port, binding);
    }

}
