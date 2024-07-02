package dev.dynamic.jobboard.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import dev.dynamic.jobboard.JobBoardBackendApplication;
import dev.dynamic.jobboard.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtUtil.class);

    private static final String TOKEN_HEADER = "Authorization";
    private static  final String TOKEN_PREFIX = "Bearer ";
    private static final String SECRET = JobBoardBackendApplication.dotenv.get("HMAC256_SECRET");

    public JwtUtil() {

    }

    public String createToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            String token = JWT.create()
                    .withClaim("email", user.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days
                    .withIssuer("JobBoard")
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            logger.error("Error creating token: {}", exception.getMessage());
            return null;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("JobBoard")
                    .build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            logger.error("Error verifying token: {}", exception.getMessage());
            return false;
        }
    }

    public String getEmail(DecodedJWT jwt) {
        return jwt.getClaim("email").asString();
    }

    public DecodedJWT resolveClaims(String token) {
        return JWT.decode(token);
    }
}
