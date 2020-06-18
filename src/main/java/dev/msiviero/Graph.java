package dev.msiviero;

import dagger.Component;
import dev.msiviero.api.SecurityApi;
import dev.msiviero.api.UserApi;

@Component
public interface Graph {

    SecurityApi securityApi();

    UserApi userApi();
}
