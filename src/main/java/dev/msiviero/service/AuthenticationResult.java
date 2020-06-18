package dev.msiviero.service;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AuthenticationResult {

    public static AuthenticationResult create(final boolean successful, final String token) {
        return new AutoValue_AuthenticationResult(successful, token);
    }

    public abstract boolean successful();

    public abstract String token();
}
