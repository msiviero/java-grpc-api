package dev.msiviero.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class JsonModule {

    @Provides
    @Singleton
    public static Gson provideGson() {

        return new GsonBuilder()
            .serializeNulls()
            .create();
    }
}
