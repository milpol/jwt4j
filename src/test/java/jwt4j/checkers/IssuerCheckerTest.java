package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.exceptions.InvalidIssuerException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IssuerCheckerTest
{
    private static final String ISSUER = "issuers";
    private static final String INVALID_ISSUER = "invalid_issuer";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private IssuerChecker issuerChecker = new IssuerChecker(ISSUER);

    @Test
    public void shouldFailWhenIssuerNotAvailable()
    {
        //expect
        expectedException.expect(InvalidIssuerException.class);
        //given
        JsonObject jsonObject = new JsonObject();
        //when
        issuerChecker.check(jsonObject);
    }

    @Test
    public void shouldFailForInvalidIssuer()
    {
        //expect
        expectedException.expect(InvalidIssuerException.class);
        //given
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.ISSUER, INVALID_ISSUER);
        //when
        issuerChecker.check(jsonObject);
    }

    @Test
    public void shouldPassForValidIssuer()
    {
        //given
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.ISSUER, ISSUER);
        //when
        issuerChecker.check(jsonObject);
        //then
    }
}