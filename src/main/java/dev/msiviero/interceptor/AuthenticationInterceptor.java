package dev.msiviero.interceptor;

import com.google.common.flogger.FluentLogger;
import dev.msiviero.service.auth.SecurityService;
import dev.msiviero.service.auth.User;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthenticationInterceptor implements ServerInterceptor {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static final Context.Key<String> AUTH_CONTEXT_ID = Context.key("AUTH_CONTEXT_ID");
    public static final Context.Key<String> AUTH_CONTEXT_USERNAME = Context.key("AUTH_CONTEXT_USERNAME");
    private final SecurityService securityService;

    @Inject
    public AuthenticationInterceptor(final SecurityService securityService) {
        this.securityService = securityService;
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

        final String authHeader = headers.get(Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));

        if (authHeader == null || authHeader.isEmpty()) {
            throw new StatusRuntimeException(Status
                .fromCode(Status.Code.FAILED_PRECONDITION)
                .augmentDescription("Invalid token"));
        }

        final User user = securityService.deserializeToken(authHeader);

        logger.atInfo().log("Recognized user  %s", user);

        final Context context = Context
            .current()
            .withValue(AUTH_CONTEXT_USERNAME, user.username())
            .withValue(AUTH_CONTEXT_ID, String.valueOf(user.id()));

        return Contexts.interceptCall(context, call, headers, next);
    }
}
