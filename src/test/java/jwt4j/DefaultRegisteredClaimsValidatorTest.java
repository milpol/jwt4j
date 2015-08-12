package jwt4j;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

public class DefaultRegisteredClaimsValidatorTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DefaultRegisteredClaimsValidator registeredClaimsValidator = new DefaultRegisteredClaimsValidator();

    @Test
    public void shouldIgnoreUnregisteredClaims()
    {
        //given
        final HashMap<String, String> customClaims = new HashMap<String, String>()
        {
            {
                put("custom-claim", "custom-claim-value");
                put("custom-claim-null", null);
            }
        };
        //when
        registeredClaimsValidator.validate(customClaims);
    }

    @Test
    public void shouldFailForNullValue()
    {
        //expect
        expectedException.expect(IllegalArgumentException.class);
        //given
        final HashMap<String, String> claims = new HashMap<String, String>()
        {
            {
                put(JWTConstants.ISSUER, null);
            }
        };
        //when
        registeredClaimsValidator.validate(claims);
    }

    @Test
    public void shouldFailForMalformedUriValue()
    {
        //expect
        expectedException.expect(IllegalArgumentException.class);
        //given
        final HashMap<String, String> claims = new HashMap<String, String>()
        {
            {
                put(JWTConstants.ISSUER, "://@#");
            }
        };
        //when
        registeredClaimsValidator.validate(claims);
    }

    @Test
    public void shouldFailForMalformedDateNumberValue()
    {
        //expect
        expectedException.expect(IllegalArgumentException.class);
        //given
        final HashMap<String, String> claims = new HashMap<String, String>()
        {
            {
                put(JWTConstants.EXPIRATION, "one");
            }
        };
        //when
        registeredClaimsValidator.validate(claims);
    }
}