package dev.msiviero.service.gravatar;

import static com.google.common.truth.Truth.assertThat;

import dev.msiviero.module.ClientModule;
import org.junit.Test;

public class GravatarServiceIntegrationTest {

    @Test
    public void shouldCallApi() {
        assertThat(new GravatarService(ClientModule.provideGravatarClient())
            .getUserProfile("m.siviero83@gmail.com").entry())
            .isNotEmpty();
    }
}