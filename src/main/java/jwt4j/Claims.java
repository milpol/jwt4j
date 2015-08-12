package jwt4j;

import java.util.Collections;
import java.util.Map;

public class Claims
{
    static final Claims NO_CLAIMS = new Claims(Collections.emptyMap());

    private final Map<String, String> claimsMap;

    Claims(Map<String, String> claimsMap)
    {
        this.claimsMap = Collections.unmodifiableMap(claimsMap);
    }

    public Map<String, String> get()
    {
        return claimsMap;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Claims claims = (Claims) o;

        if (claimsMap != null ? !claimsMap.equals(claims.claimsMap) : claims.claimsMap != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return claimsMap != null ? claimsMap.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "Claims{" +
                "claimsMap=" + claimsMap +
                '}';
    }
}