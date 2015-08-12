package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.exceptions.InvalidAudienceException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AudienceCheckerTest
{
    private static final String AUDIENCE = "audience";
    private static final String INVALID_AUDIENCE = "invalid_audience";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private AudienceChecker audienceChecker = new AudienceChecker(AUDIENCE);

    @Test
    public void shouldFailWhenAudienceNotAvailable()
    {
        //expect
        expectedException.expect(InvalidAudienceException.class);
        //given
        JsonObject jsonObject = new JsonObject();
        //when
        audienceChecker.check(jsonObject);
    }

    @Test
    public void shouldFailForInvalidAudience()
    {
        //expect
        expectedException.expect(InvalidAudienceException.class);
        //given
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.AUDIENCE, INVALID_AUDIENCE);
        //when
        audienceChecker.check(jsonObject);
    }

    @Test
    public void shouldPassForValidAudience()
    {
        //given
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.AUDIENCE, AUDIENCE);
        //when
        audienceChecker.check(jsonObject);
        //then
    }
}