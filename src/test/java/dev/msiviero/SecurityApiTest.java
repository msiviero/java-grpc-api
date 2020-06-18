package dev.msiviero;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dev.msiviero.api.SecurityApi;
import dev.msiviero.api.SecurityGrpc;
import dev.msiviero.api.TokenRequest;
import dev.msiviero.api.TokenResponse;
import dev.msiviero.service.AuthenticationResult;
import dev.msiviero.service.SecurityService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SecurityApiTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Mock
    SecurityService securityService;

    @Before
    public void setUp() {
        when(securityService.authenticate(anyString(), anyString()))
            .thenReturn(AuthenticationResult.create(false, ""));
    }

    @Test
    public void shouldReturnATokenGivenValidCredentials() throws IOException {
        when(securityService.authenticate("m.siviero83@gmail.com", "my-secret-password"))
            .thenReturn(AuthenticationResult.create(true, "abcd"));

        final String serverName = InProcessServerBuilder.generateName();

        final Server inProcessServer = InProcessServerBuilder
            .forName(serverName)
            .directExecutor()
            .addService(new SecurityApi(securityService))
            .build()
            .start();
        grpcCleanup.register(inProcessServer);

        final ManagedChannel channel = InProcessChannelBuilder
            .forName(serverName)
            .directExecutor()
            .build();
        grpcCleanup.register(channel);

        final TokenResponse response = SecurityGrpc
            .newBlockingStub(channel)
            .generateToken(TokenRequest.newBuilder()
                .setUsername("m.siviero83@gmail.com")
                .setPassword("my-secret-password")
                .build());

        assertThat(response.getToken()).isEqualTo("abcd");
    }

    @Test
    public void shouldReturnNotFoundGivenWrongCredentials() throws IOException {
        final String serverName = InProcessServerBuilder.generateName();

        final Server inProcessServer = InProcessServerBuilder
            .forName(serverName)
            .directExecutor()
            .addService(new SecurityApi(securityService))
            .build()
            .start();
        grpcCleanup.register(inProcessServer);

        final ManagedChannel channel = InProcessChannelBuilder
            .forName(serverName)
            .directExecutor()
            .build();
        grpcCleanup.register(channel);

        try {
            //noinspection ResultOfMethodCallIgnored
            SecurityGrpc
                .newBlockingStub(channel)
                .generateToken(TokenRequest
                    .newBuilder()
                    .setUsername("m.siviero83@gmail.com")
                    .setPassword("my-secret-password")
                    .build());

            fail("Should throw exception");

        } catch (final StatusRuntimeException exception) {
            assertThat(exception.getStatus().getCode()).isEqualTo(Code.NOT_FOUND);
        }
    }
}