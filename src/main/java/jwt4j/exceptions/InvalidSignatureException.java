package jwt4j.exceptions;

public class InvalidSignatureException extends InvalidTokenException
{
    public InvalidSignatureException()
    {
    }

    public InvalidSignatureException(String s)
    {
        super(s);
    }

    public InvalidSignatureException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
