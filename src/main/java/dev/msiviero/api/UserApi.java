package dev.msiviero.api;


import static dev.msiviero.interceptor.AuthenticationInterceptor.AUTH_CONTEXT_USERNAME;

import dev.msiviero.api.UserGrpc.UserImplBase;
import io.grpc.stub.StreamObserver;
import javax.inject.Inject;

public class UserApi extends UserImplBase {

    @Inject
    public UserApi() {
    }

    @Override
    public void myUser(final MyUserRequest request, final StreamObserver<MyUserResponse> responseObserver) {
        final MyUserResponse reply = MyUserResponse
            .newBuilder()
            .setUsername(AUTH_CONTEXT_USERNAME.get())
            .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
