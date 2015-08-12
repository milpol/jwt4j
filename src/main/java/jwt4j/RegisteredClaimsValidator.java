package jwt4j;

import java.util.Map;

public interface RegisteredClaimsValidator
{
    public static final RegisteredClaimsValidator DISABLED = new DisabledRegisteredClaimsValidator();

    public static final RegisteredClaimsValidator DEFAULT = new DefaultRegisteredClaimsValidator();

    void validate(Map<String, String> claims);
}