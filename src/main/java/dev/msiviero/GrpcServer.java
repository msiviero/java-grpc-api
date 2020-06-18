package dev.msiviero;


import com.google.common.flogger.FluentLogger;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class GrpcServer {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final List<BindableService> services;
    private Server server;

    private GrpcServer(final List<BindableService> services) {
        this.services = services;
    }

    public void start() throws IOException {

        final int port = 8000;
        final ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);

        for (final BindableService service : services) {
            serverBuilder.addService(service);
        }

        server = serverBuilder.build();
        server.start();
        logger.atInfo().log("Server started on port: %s", port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                GrpcServer.this.stop();
            } catch (final InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        List<BindableService> services = new ArrayList<>();

        public Builder addService(final BindableService service) {
            services.add(service);
            return this;
        }

        public GrpcServer build() {
            return new GrpcServer(services);
        }
    }

}

