package dev.msiviero.module;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dagger.Module;
import dagger.Provides;
import dev.msiviero.config.DatabaseConfiguration;
import javax.inject.Singleton;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Module
public class PersistenceModule {

    @Provides
    @Singleton
    public static NamedParameterJdbcTemplate provideNamedJdbcTemplate(final DatabaseConfiguration configuration) {

        final String jdbcUrl = String.format("jdbc:mysql://%s/%s",
            configuration.databaseHost(),
            configuration.databaseName()
        );

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(configuration.databaseUsername());
        config.setPassword(configuration.databasePassword());

        return new NamedParameterJdbcTemplate(new HikariDataSource(config));
    }
}
