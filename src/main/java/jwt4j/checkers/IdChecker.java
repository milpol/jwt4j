package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.TokenChecker;
import jwt4j.exceptions.InvalidTokenException;

public class IdChecker implements TokenChecker
{
    @Override
    public void check(JsonObject payloadJson)
    {
        if (!payloadJson.has(JWTConstants.JWT_ID) ||
                "".equals(payloadJson.get(JWTConstants.JWT_ID).getAsString())) {
            throw new InvalidTokenException("Required JWT id (jti) claim not found");
        }
    }
}