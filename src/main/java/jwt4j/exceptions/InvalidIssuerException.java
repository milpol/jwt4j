package jwt4j.exceptions;

public class InvalidIssuerException extends InvalidTokenException
{
    public InvalidIssuerException()
    {
    }

    public InvalidIssuerException(String s)
    {
        super(s);
    }

    public InvalidIssuerException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
