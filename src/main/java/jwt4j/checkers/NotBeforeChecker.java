package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.TokenChecker;
import jwt4j.exceptions.InvalidTokenException;

import java.time.Instant;


public class NotBeforeChecker implements TokenChecker
{
    private final int leewaySeconds;

    public NotBeforeChecker(final int leewaySeconds)
    {
        this.leewaySeconds = leewaySeconds;
    }

    @Override
    public void check(JsonObject payloadJson)
    {
        if (payloadJson.has(JWTConstants.NOT_BEFORE)) {
            long timeout = payloadJson.get(JWTConstants.NOT_BEFORE).getAsLong() -
                    Instant.now().getEpochSecond() -
                    leewaySeconds;
            if (timeout > 0) {
                throw new InvalidTokenException("Token will be valid in " + timeout + " seconds");
            }
        } else {
            throw new InvalidTokenException("Required not before (nbf) claim not found");
        }
    }
}
