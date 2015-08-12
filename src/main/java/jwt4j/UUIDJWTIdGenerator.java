package jwt4j;

import java.util.UUID;

public class UUIDJWTIdGenerator implements JWTIdGenerator
{
    @Override
    public String getJwtId()
    {
        return UUID.randomUUID().toString();
    }
}
