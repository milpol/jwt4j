package jwt4j;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

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
        if (!(o instanceof Claims)) return false;
        Claims claims = (Claims) o;
        return Objects.equals(claimsMap, claims.claimsMap);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(claimsMap);
    }

    @Override
    public String toString()
    {
        return "Claims{" +
                "claimsMap=" + claimsMap +
                '}';
    }
}