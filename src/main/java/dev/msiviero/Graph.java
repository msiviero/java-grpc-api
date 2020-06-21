package dev.msiviero;

import dagger.Component;
import dev.msiviero.api.SecurityApi;
import dev.msiviero.api.UserApi;
import dev.msiviero.interceptor.AuthenticationInterceptor;
import dev.msiviero.module.PersistenceModule;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    PersistenceModule.class
})
public interface Graph {

    SecurityApi securityApi();

    UserApi userApi();

    AuthenticationInterceptor authenticationInterceptor();
}
