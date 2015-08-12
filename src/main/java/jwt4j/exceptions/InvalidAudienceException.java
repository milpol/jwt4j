package jwt4j.exceptions;

public class InvalidAudienceException extends InvalidTokenException
{
    public InvalidAudienceException()
    {
    }

    public InvalidAudienceException(String s)
    {
        super(s);
    }

    public InvalidAudienceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}