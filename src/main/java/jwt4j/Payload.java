package jwt4j;

import java.util.Objects;

public class Payload<T>
{
    private final T data;

    private final Claims claims;

    public Payload(T data, Claims claims)
    {
        this.data = data;
        this.claims = claims;
    }

    public T getData()
    {
        return data;
    }

    public Claims getClaims()
    {
        return claims;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Payload)) return false;
        Payload<?> payload = (Payload<?>) o;
        return Objects.equals(data, payload.data) &&
                Objects.equals(claims, payload.claims);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(data, claims);
    }

    @Override
    public String toString()
    {
        return "Payload{" +
                "data=" + data +
                ", claims=" + claims +
                '}';
    }
}