package dev.msiviero.service.auth;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class User {

    public abstract int id();

    public abstract String username();

    public static User create(final int id, final String username) {
        return new AutoValue_User(id, username);
    }
}
