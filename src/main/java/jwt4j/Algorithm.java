package jwt4j;

public enum Algorithm
{
    none(""),
    HS256("HmacSHA256"),
    HS384("HmacSHA384"),
    HS512("HmacSHA512");

    public final String name;

    private Algorithm(String name)
    {
        this.name = name;
    }
}