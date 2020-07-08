package dev.msiviero.api;


import static dev.msiviero.interceptor.AuthenticationInterceptor.AUTH_CONTEXT_USERNAME;

import dev.msiviero.api.UserGrpc.UserImplBase;
import dev.msiviero.service.gravatar.GravatarService;
import io.grpc.stub.StreamObserver;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserApi extends UserImplBase {

    private final GravatarService gravatarService;

    @Inject
    public UserApi(final GravatarService gravatarService) {
        this.gravatarService = gravatarService;
    }

    @Override
    public void myUser(final MyUserRequest request, final StreamObserver<MyUserResponse> responseObserver) {
        final String email = AUTH_CONTEXT_USERNAME.get();

        final MyUserResponse reply = MyUserResponse
            .newBuilder()
            .setUsername(email)
            .setDisplayName(gravatarService.getUserProfile(email).entry().get(0).displayName())
            .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
