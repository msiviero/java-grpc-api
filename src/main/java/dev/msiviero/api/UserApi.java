package dev.msiviero.api;

import dev.msiviero.api.UserGrpc.UserImplBase;
import io.grpc.stub.StreamObserver;
import javax.inject.Inject;

public class UserApi extends UserImplBase {

    @Inject
    public UserApi() {
    }

    @Override
    public void myUser(final MyUserRequest request, final StreamObserver<MyUserResponse> responseObserver) {
        super.myUser(request, responseObserver);
    }
}
