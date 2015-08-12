package jwt4j;

public final class JWTConstants
{
    public static final String JWT = "JWT";
    public static final String DELIMITER = ".";

    public static final String TYPE = "typ";
    public static final String ALGORITHM = "alg";
    public static final String ISSUER = "iss";
    public static final String SUBJECT = "sub";
    public static final String AUDIENCE = "aud";
    public static final String EXPIRATION = "exp";
    public static final String NOT_BEFORE = "nbf";
    public static final String ISSUED_AT = "iat";
    public static final String JWT_ID = "jti";
    public static final String DATA = "data4j";


    public static final String[] CLAIM_NAMES = new String[]{
            ISSUER, SUBJECT, AUDIENCE, EXPIRATION, NOT_BEFORE, ISSUED_AT, JWT_ID, DATA};
}