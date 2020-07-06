package dev.msiviero.service.auth;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SecurityServiceTest {
    
    @InjectMocks
    private SecurityService underTest;

    @Test
    public void shouldSerializeAuthToken() {

        final String serialized = underTest.serializeToken(User.create(999, "user@example.com"));
        final User deSerialized = underTest.deserializeToken(serialized);

        assertThat(deSerialized.id()).isEqualTo(999);
        assertThat(deSerialized.username()).isEqualTo("user@example.com");
    }
}