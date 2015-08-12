package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.TokenChecker;
import jwt4j.exceptions.ExpiredTokenException;
import jwt4j.exceptions.InvalidTokenException;

import java.time.Instant;

public class ExpirationIssuedAtChecker implements TokenChecker
{
    private final int expirationSeconds;

    private final int leewaySeconds;

    public ExpirationIssuedAtChecker(final int expirationSeconds,
                                     final int leewaySeconds)
    {
        this.expirationSeconds = expirationSeconds;
        this.leewaySeconds = leewaySeconds;
    }

    @Override
    public void check(final JsonObject payloadJson)
    {
        final long timeout;
        if (payloadJson.has(JWTConstants.EXPIRATION)) {
            if (payloadJson.has(JWTConstants.ISSUED_AT)) {
                timeout = Instant.now().getEpochSecond() -
                        payloadJson.get(JWTConstants.ISSUED_AT).getAsInt() -
                        expirationSeconds -
                        leewaySeconds;
            } else {
                throw new InvalidTokenException("Required issued at (iat) claim not found");
            }
        } else {
            throw new InvalidTokenException("Required expiration (exp) claim not found");
        }
        if (timeout > 0) {
            throw new ExpiredTokenException("Expired of " + timeout + " seconds");
        }
    }
}