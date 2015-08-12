package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.TokenChecker;
import jwt4j.exceptions.ExpiredTokenException;
import jwt4j.exceptions.InvalidTokenException;

import java.time.Instant;

public class ExpirationChecker implements TokenChecker
{
    private final int leewaySeconds;

    public ExpirationChecker(final int leewaySeconds)
    {
        this.leewaySeconds = leewaySeconds;
    }

    @Override
    public void check(JsonObject payloadJson)
    {
        final long timeout;
        if (payloadJson.has(JWTConstants.EXPIRATION)) {
            timeout = Instant.now().getEpochSecond() -
                    payloadJson.get(JWTConstants.EXPIRATION).getAsInt() -
                    leewaySeconds;
        } else {
            throw new InvalidTokenException("Required expiration (exp) claim not found");
        }
        if (timeout > 0) {
            throw new ExpiredTokenException("Expired of " + timeout + " seconds");
        }
    }
}