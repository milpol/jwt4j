package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.exceptions.ExpiredTokenException;
import jwt4j.exceptions.InvalidTokenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Instant;

public class ExpirationCheckerTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ExpirationChecker expirationChecker = new ExpirationChecker(5);

    @Test
    public void shouldFailForInvalidTokenValue()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        //given
        final JsonObject jsonObject = new JsonObject();
        //when
        expirationChecker.check(jsonObject);
    }

    @Test
    public void shouldFailForExpiredToken()
    {
        //expect
        expectedException.expect(ExpiredTokenException.class);
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.EXPIRATION, Instant.now().minusSeconds(7).getEpochSecond());
        //when
        expirationChecker.check(jsonObject);
    }

    @Test
    public void shouldValidateTokenWithLeewayCheck()
    {
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.EXPIRATION, Instant.now().plusSeconds(7).getEpochSecond());
        //when
        expirationChecker.check(jsonObject);
    }
}