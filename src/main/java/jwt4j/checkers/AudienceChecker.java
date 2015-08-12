package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.TokenChecker;
import jwt4j.exceptions.InvalidAudienceException;

public class AudienceChecker implements TokenChecker
{
    private final String audience;

    public AudienceChecker(String audience)
    {
        this.audience = audience;
    }

    @Override
    public void check(final JsonObject payloadJson)
    {
        if (!payloadJson.has(JWTConstants.AUDIENCE) ||
                !payloadJson.get(JWTConstants.AUDIENCE).getAsString().equals(audience)) {
            throw new InvalidAudienceException("Expected " + audience + " audience");
        }
    }
}