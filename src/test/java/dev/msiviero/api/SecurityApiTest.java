package dev.msiviero.api;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dev.msiviero.service.auth.AuthenticationResult;
import dev.msiviero.service.auth.SecurityService;
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
    SecurityService securityServiceMock;

    private ManagedChannel channel;

    @Before
    public void setUp() throws IOException {
        when(securityServiceMock.authenticate(anyString(), anyString()))
            .thenReturn(AuthenticationResult.create(false, ""));

        final String serverName = InProcessServerBuilder.generateName();

        final Server inProcessServer = InProcessServerBuilder
            .forName(serverName)
            .directExecutor()
            .addService(new SecurityApi(securityServiceMock))
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
    public void shouldReturnATokenGivenValidCredentials() throws IOException {
        when(securityServiceMock.authenticate("m.siviero83@gmail.com", "my-secret-password"))
            .thenReturn(AuthenticationResult.create(true, "abcd"));

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
            assertThat(exception.getStatus().getCode()).isEqualTo(Code.FAILED_PRECONDITION);
        }
    }
}