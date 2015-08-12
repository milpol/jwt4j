package jwt4j;

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
        if (o == null || getClass() != o.getClass()) return false;

        Payload payload = (Payload) o;

        if (claims != null ? !claims.equals(payload.claims) : payload.claims != null) return false;
        if (data != null ? !data.equals(payload.data) : payload.data != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = data != null ? data.hashCode() : 0;
        result = 31 * result + (claims != null ? claims.hashCode() : 0);
        return result;
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