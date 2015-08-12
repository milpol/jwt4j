package jwt4j.exceptions;

public class InvalidTokenException extends IllegalArgumentException
{
    public InvalidTokenException()
    {
    }

    public InvalidTokenException(String s)
    {
        super(s);
    }

    public InvalidTokenException(String message, Throwable cause)
    {
        super(message, cause);
    }
}