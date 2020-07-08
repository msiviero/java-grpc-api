package dev.msiviero.service.gravatar;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import dev.msiviero.client.GravatarClient;
import dev.msiviero.client.GravatarResponse;
import java.io.IOException;
import javax.inject.Inject;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class GravatarService {

    private final GravatarClient gravatarClient;

    @Inject
    GravatarService(final GravatarClient gravatarClient) {
        this.gravatarClient = gravatarClient;
    }

    final static private HashFunction hashingFunction = Hashing.md5();

    public GravatarResponse getUserProfile(final String email) {

        final String hash = hashingFunction
            .hashString(email.trim().toLowerCase(), Charsets.UTF_8).toString();

        try {
            return gravatarClient.userProfile(hash).execute().body();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
