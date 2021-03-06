/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package dev.msiviero;

import java.io.IOException;

public class App {


    public static void main(final String[] args) throws IOException, InterruptedException {

        final Graph graph = DaggerGraph.create();

        final GrpcServer server = GrpcServer
            .builder()
            .addService(graph.securityApi())
            .addService(graph.userApi())
            .addInterceptor(graph.authenticationInterceptor())
            .build();

        server.start();
        server.blockUntilShutdown();
    }
}
