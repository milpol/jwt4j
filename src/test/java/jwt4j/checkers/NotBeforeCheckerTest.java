package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.exceptions.InvalidTokenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Instant;

public class NotBeforeCheckerTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private NotBeforeChecker notBeforeChecker = new NotBeforeChecker(5);

    @Test
    public void shouldFailForInvalidTokenValue()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        //given
        final JsonObject jsonObject = new JsonObject();
        //when
        notBeforeChecker.check(jsonObject);
    }

    @Test
    public void shouldFailForNotBeforeToken()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.NOT_BEFORE, Instant.now().plusSeconds(7).getEpochSecond());
        //when
        notBeforeChecker.check(jsonObject);
    }

    @Test
    public void shouldValidateTokenWithLeewayCheck()
    {
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.NOT_BEFORE, Instant.now().plusSeconds(4).getEpochSecond());
        //when
        notBeforeChecker.check(jsonObject);
    }
}