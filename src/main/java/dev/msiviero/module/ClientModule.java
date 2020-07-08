package dev.msiviero.module;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import dev.msiviero.client.GravatarClient;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class ClientModule {

    @Provides
    @Singleton
    public static GravatarClient provideGravatarClient() {

        final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.gravatar.com")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build();

        return retrofit.create(GravatarClient.class);
    }
}
