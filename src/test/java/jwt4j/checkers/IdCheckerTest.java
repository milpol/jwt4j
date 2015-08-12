package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.exceptions.InvalidTokenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IdCheckerTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private IdChecker idChecker = new IdChecker();

    @Test
    public void shouldFailForInvalidTokenStructure()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        //given
        final JsonObject jsonObject = new JsonObject();
        //when
        idChecker.check(jsonObject);
    }

    @Test
    public void shouldFailForEmptyId()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.JWT_ID, "");
        //when
        idChecker.check(jsonObject);
    }

    @Test
    public void shouldValidateToken()
    {
        //given
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.JWT_ID, "id");
        //when
        idChecker.check(jsonObject);
    }
}