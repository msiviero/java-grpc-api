package dev.msiviero.repository;

import com.google.common.base.Preconditions;
import dev.msiviero.service.auth.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {

        final int id = resultSet.getInt("id");
        final String username = resultSet.getString("username");
        final String token = resultSet.getString("token");

        Preconditions.checkNotNull(username);
        Preconditions.checkNotNull(token);

        return User.create(id, username);
    }
}