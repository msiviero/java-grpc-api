package dev.msiviero.api;

import static com.google.common.truth.Truth.assertThat;

import dev.msiviero.interceptor.AuthenticationInterceptor;
import dev.msiviero.service.auth.SecurityService;
import dev.msiviero.service.gravatar.GravatarService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import java.io.IOException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserApiTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Mock
    private SecurityService securityService;

    @Mock
    private GravatarService gravatarService;

    private ManagedChannel channel;

    @Before
    public void setUp() throws IOException {
        final String serverName = InProcessServerBuilder.generateName();

        final Server inProcessServer = InProcessServerBuilder
            .forName(serverName)
            .directExecutor()
            .addService(new UserApi(gravatarService))
            .intercept(new AuthenticationInterceptor(securityService))
            .build()
            .start();
        grpcCleanup.register(inProcessServer);

        channel = InProcessChannelBuilder
            .forName(serverName)
            .directExecutor()
            .build();
        grpcCleanup.register(channel);
    }

    @Test
    @Ignore
    public void shouldXX() {
        final MyUserResponse response = UserGrpc
            .newBlockingStub(channel)
            .myUser(MyUserRequest
                .newBuilder()
                .build());

        assertThat(response.getUsername()).isEqualTo("blabla");
    }
}