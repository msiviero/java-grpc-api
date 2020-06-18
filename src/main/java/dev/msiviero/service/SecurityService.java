package dev.msiviero.service;

import javax.inject.Inject;

public class SecurityService {

    @Inject
    SecurityService() {
    }

    public AuthenticationResult authenticate(final String username, final String password) {
        return AuthenticationResult.create(true, "a-token");
    }
}
