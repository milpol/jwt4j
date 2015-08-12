package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.TokenChecker;
import jwt4j.exceptions.InvalidTokenException;

public class SubjectChecker implements TokenChecker
{
    private final String subject;

    public SubjectChecker(final String subject)
    {
        this.subject = subject;
    }

    @Override
    public void check(JsonObject payloadJson)
    {
        if (!payloadJson.has(JWTConstants.SUBJECT) ||
                !payloadJson.get(JWTConstants.SUBJECT).getAsString().equals(subject)) {
            throw new InvalidTokenException("Expected " + subject + " subject");
        }
    }
}