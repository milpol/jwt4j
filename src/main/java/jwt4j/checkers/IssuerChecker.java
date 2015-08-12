package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.TokenChecker;
import jwt4j.exceptions.InvalidIssuerException;

import static jwt4j.JWTConstants.ISSUER;

public class IssuerChecker implements TokenChecker
{
    private final String issuer;

    public IssuerChecker(final String issuer)
    {
        this.issuer = issuer;
    }

    @Override
    public void check(JsonObject payloadJson)
    {
        if (!payloadJson.has(ISSUER) || !payloadJson.get(ISSUER).getAsString().equals(issuer)) {
            throw new InvalidIssuerException("Expected " + issuer + " issuer");
        }
    }
}