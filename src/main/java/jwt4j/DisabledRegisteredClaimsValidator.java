package jwt4j;

import java.util.Map;

public class DisabledRegisteredClaimsValidator implements RegisteredClaimsValidator
{
    @Override
    public void validate(final Map<String, String> claims)
    {
    }
}
