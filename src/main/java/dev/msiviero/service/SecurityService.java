package dev.msiviero.service;


import com.google.common.flogger.FluentLogger;
import dev.msiviero.entity.UserEntity;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;
import java.util.HashMap;
import javax.inject.Inject;

public class SecurityService {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final EntityDataStore<Persistable> store;

    @Inject
    SecurityService(final EntityDataStore<Persistable> store) {
        this.store = store;
    }

    public AuthenticationResult authenticate(final String username, final String password) {

        final UserEntity user = store
            .select(UserEntity.class)
            .where(UserEntity.USERNAME.eq(username))
            .and(UserEntity.PASSWORD.eq(password))
            .get()
            .firstOrNull();

        if (user != null) {
            logger.atInfo().log("User %s recognized. Updating token", username);

            final String token = Jwts.builder()
                .setHeaderParam(JwsHeader.ALGORITHM, SignatureAlgorithm.HS256)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(new HashMap<>())
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
                .compact();

            user.token(token);
            store.update(user);

            return AuthenticationResult.create(true, token);
        }

        logger.atInfo().log("User %s not recognized", username);
        return AuthenticationResult.create(false, "");
    }
}
