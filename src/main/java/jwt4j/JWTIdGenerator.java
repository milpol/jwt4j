package jwt4j;

public interface JWTIdGenerator
{
    public static final JWTIdGenerator UUID_JWT_ID = new UUIDJWTIdGenerator();

    String getJwtId();
}