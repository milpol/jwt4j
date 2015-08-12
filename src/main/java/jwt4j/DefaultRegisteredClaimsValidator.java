package jwt4j;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultRegisteredClaimsValidator implements RegisteredClaimsValidator
{
    private static final Map<String, Consumer<String>> CLAIMS_VALIDATOR = new HashMap<>();

    private static final Consumer<String> STRING_OR_URI = value -> {
        if (value == null) {
            throw new IllegalArgumentException("Claim defined but no value specified.");
        }
        if (value.contains("://")) {
            try {
                URI.create(value);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid URI format for claim value.");
            }
        }
    };

    private static final Consumer<String> NUMERIC_DATE = value -> {
        try {
            Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric date format for claim value.");
        }
    };

    private static final Consumer<String> UNRESTRICTED = value -> {

    };

    static {
        CLAIMS_VALIDATOR.put(JWTConstants.ISSUER, STRING_OR_URI);
        CLAIMS_VALIDATOR.put(JWTConstants.SUBJECT, STRING_OR_URI);
        CLAIMS_VALIDATOR.put(JWTConstants.AUDIENCE, STRING_OR_URI);
        CLAIMS_VALIDATOR.put(JWTConstants.EXPIRATION, NUMERIC_DATE);
        CLAIMS_VALIDATOR.put(JWTConstants.NOT_BEFORE, NUMERIC_DATE);
        CLAIMS_VALIDATOR.put(JWTConstants.ISSUED_AT, NUMERIC_DATE);
        CLAIMS_VALIDATOR.put(JWTConstants.JWT_ID, UNRESTRICTED);
    }

    @Override
    public void validate(final Map<String, String> claims)
    {
        if (claims != null) {
            claims.forEach((claim, value) -> CLAIMS_VALIDATOR.getOrDefault(claim, UNRESTRICTED).accept(value));
        }
    }
}
