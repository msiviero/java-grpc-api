package dev.msiviero.service.auth;


import com.google.common.collect.ImmutableMap;
import com.google.common.flogger.FluentLogger;
import dev.msiviero.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SecurityService {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final UserRepository userRepository;
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Inject
    SecurityService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User deserializeToken(final String authToken) {
        final Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(authToken)
            .getBody();

        return User.create(
            claims.get("id", Integer.class),
            claims.get("username", String.class)
        );
    }

    public String serializeToken(final User user) {
        return Jwts
            .builder()
            .setHeaderParam(JwsHeader.ALGORITHM, SignatureAlgorithm.HS256)
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(ImmutableMap
                .<String, Object>builder()
                .put("id", user.id())
                .put("username", user.username())
                .build())
            .signWith(secretKey)
            .compact();
    }

    public AuthenticationResult authenticate(final String username, final String password) {
        return userRepository
            .findByUsernameAndPassword(username, password)
            .map((user -> {
                logger.atInfo().log("User %s recognized. Returning token", username);
                return AuthenticationResult.create(true, serializeToken(user));
            }))
            .orElseGet(() -> {
                logger.atInfo().log("User %s not recognized", username);
                return AuthenticationResult.create(false, "");
            });
    }
}
