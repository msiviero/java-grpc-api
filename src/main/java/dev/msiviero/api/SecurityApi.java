package dev.msiviero.api;

import dev.msiviero.api.SecurityGrpc.SecurityImplBase;
import dev.msiviero.service.AuthenticationResult;
import dev.msiviero.service.SecurityService;
import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.stub.StreamObserver;
import javax.inject.Inject;

public class SecurityApi extends SecurityImplBase {

    private final SecurityService securityService;

    @Inject
    public SecurityApi(final SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void generateToken(final TokenRequest request, final StreamObserver<TokenResponse> responseObserver) {

        final AuthenticationResult authResult = securityService.authenticate(request.getUsername(), request.getPassword());

        if (authResult.successful()) {
            final TokenResponse reply = TokenResponse.newBuilder()
                .setToken(authResult.token())
                .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        } else {
            final Status status = Status
                .fromCode(Code.NOT_FOUND)
                .withDescription("Invalid credentials");
            responseObserver.onError(status.asException());
        }
    }
}
