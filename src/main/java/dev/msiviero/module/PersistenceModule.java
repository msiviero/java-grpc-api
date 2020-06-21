package dev.msiviero.module;

import static dev.msiviero.entity.Models.DEFAULT;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dagger.Module;
import dagger.Provides;
import dev.msiviero.config.DatabaseConfiguration;
import io.requery.Persistable;
import io.requery.cache.WeakEntityCache;
import io.requery.sql.ConfigurationBuilder;
import io.requery.sql.EntityDataStore;
import java.util.concurrent.Executors;
import javax.inject.Singleton;

@Module
public class PersistenceModule {

    @Provides
    @Singleton
    public static EntityDataStore<Persistable> provideDataStore(final DatabaseConfiguration configuration) {

        final String jdbcUrl = String.format("jdbc:mysql://%s/%s",
            configuration.databaseHost(),
            configuration.databaseName()
        );

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(configuration.databaseUsername());
        config.setPassword(configuration.databasePassword());

        final ConfigurationBuilder requeryConfigBuilder = new ConfigurationBuilder(
            new HikariDataSource(config)::getConnection,
            DEFAULT
        );
        
        return new EntityDataStore<>(requeryConfigBuilder
            .setEntityCache(new WeakEntityCache())
            .setWriteExecutor(Executors.newSingleThreadExecutor())
            .build());
    }
}
