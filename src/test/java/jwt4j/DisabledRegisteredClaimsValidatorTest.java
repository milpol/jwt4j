package jwt4j;

import org.junit.Test;

import java.util.HashMap;

public class DisabledRegisteredClaimsValidatorTest
{
    @Test
    public void shouldDoNothing()
    {
        //given
        final DisabledRegisteredClaimsValidator disabledRegisteredClaimsValidator = new DisabledRegisteredClaimsValidator();
        //when
        disabledRegisteredClaimsValidator.validate(new HashMap<String, String>()
        {
            {
                put(JWTConstants.JWT_ID, "Jwt-ID");
            }
        });
        //then
    }
}