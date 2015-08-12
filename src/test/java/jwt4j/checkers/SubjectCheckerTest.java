package jwt4j.checkers;

import com.google.gson.JsonObject;
import jwt4j.JWTConstants;
import jwt4j.exceptions.InvalidTokenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SubjectCheckerTest
{
    private static final String SUBJECT = "subject";
    private static final String INVALID_SUBJECT = "invalid_subject";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SubjectChecker subjectChecker = new SubjectChecker(SUBJECT);

    @Test
    public void shouldFailWhenSubjectNotAvailable()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        //given
        JsonObject jsonObject = new JsonObject();
        //when
        subjectChecker.check(jsonObject);
    }

    @Test
    public void shouldFailForInvalidSubject()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        //given
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.SUBJECT, INVALID_SUBJECT);
        //when
        subjectChecker.check(jsonObject);
    }

    @Test
    public void shouldPassForValidSubject()
    {
        //given
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JWTConstants.SUBJECT, SUBJECT);
        //when
        subjectChecker.check(jsonObject);
        //then
    }
}