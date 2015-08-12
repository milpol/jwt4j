package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.exceptions.ExpiredTokenException;
import jwt4j.exceptions.InvalidTokenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Instant;

public class ExpirationIssuedAtCheckerTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ExpirationIssuedAtChecker expirationIssuedAtChecker = new ExpirationIssuedAtChecker(10, 5);

    @Test
    public void shouldFailForLackOfExpirationInfo()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.ISSUED_AT, Instant.now().getEpochSecond());
        //when
        expirationIssuedAtChecker.check(jsonObject);
    }

    @Test
    public void shouldFailForLackOfIssuedAtInfo()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.EXPIRATION, Instant.now().getEpochSecond());
        //when
        expirationIssuedAtChecker.check(jsonObject);
    }

    @Test
    public void shouldFailForExpiredToken()
    {
        //expect
        expectedException.expect(ExpiredTokenException.class);
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.ISSUED_AT, Instant.now().minusSeconds(17).getEpochSecond());
        jsonObject.addProperty(JWTConstants.EXPIRATION, Instant.now().minusSeconds(7).getEpochSecond());
        //when
        expirationIssuedAtChecker.check(jsonObject);
    }

    @Test
    public void shouldValidateTokenWithLeewayCheck()
    {
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.ISSUED_AT, Instant.now().minusSeconds(7).getEpochSecond());
        jsonObject.addProperty(JWTConstants.EXPIRATION, Instant.now().plusSeconds(7).getEpochSecond());
        //when
        expirationIssuedAtChecker.check(jsonObject);
    }
}