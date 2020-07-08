package dev.msiviero.repository;

import dagger.Reusable;
import dev.msiviero.service.auth.User;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.CheckReturnValue;
import javax.inject.Inject;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Reusable
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Inject
    public UserRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @CheckReturnValue
    public Optional<User> findByUsernameAndPassword(final String username, final String password) {

        final List<User> queryResults = jdbcTemplate
            .query("SELECT * FROM users WHERE username = :username AND password = :password",
                new MapSqlParameterSource()
                    .addValue("username", username)
                    .addValue("password", password),
                new UserMapper());

        return Optional.ofNullable(queryResults.isEmpty() ? null : queryResults.get(0));
    }

    public Optional<User> getByToken(final String token) {
        final List<User> queryResults = jdbcTemplate
            .query("SELECT * FROM users WHERE token = :token",
                new MapSqlParameterSource().addValue("token", token),
                new UserMapper());

        return Optional.ofNullable(queryResults.isEmpty()
            ? null
            : queryResults.get(0));
    }

    public void update(final User user) {

        final HashMap<Object, Object> data = new HashMap<>();

        data.put("id", user.id());
        data.put("username", user.username());

        jdbcTemplate.update(
            "UPDATE users SET username= :username, token= :token WHERE id= :id",
            new BeanPropertySqlParameterSource(data)
        );
    }
}

