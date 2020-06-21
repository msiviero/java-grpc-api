package dev.msiviero.interceptor;

import dev.msiviero.entity.UserEntity;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;
import javax.inject.Inject;

public class AuthenticationInterceptor implements ServerInterceptor {

    private final EntityDataStore<Persistable> store;

    @Inject
    AuthenticationInterceptor(final EntityDataStore<Persistable> store) {
        this.store = store;
    }

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(
        final ServerCall<ReqT, RespT> call,
        final Metadata headers,
        final ServerCallHandler<ReqT, RespT> next
    ) {
        if (call.getMethodDescriptor().getFullMethodName().equals("Security/GenerateToken")) {
            return next.startCall(call, headers);
        }

        final String authToken = headers.get(Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));

        final UserEntity user = store
            .select(UserEntity.class)
            .where(UserEntity.TOKEN.eq(authToken))
            .get()
            .firstOrNull();

        if (user != null) {
            return next.startCall(call, headers);
        }

        throw new StatusRuntimeException(Status
            .fromCode(Status.Code.FAILED_PRECONDITION)
            .augmentDescription("Invalid token")
        );
    }
}
