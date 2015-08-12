package jwt4j;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jwt4j.exceptions.ExpiredTokenException;
import jwt4j.exceptions.InvalidSignatureException;
import jwt4j.exceptions.InvalidTokenException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class JWTDecoderTest
{
    private static final String SUBJECT = "subject";
    private static final String SECRET = "secret";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final Gson gson = new Gson();

    private final Base64.Encoder encoder = Base64.getEncoder();

    @Mock
    private TokenChecker tokenChecker;

    private JWTDecoder defaultJwtDecoder;

    private JWTDecoder noneAlgorithmJwtDecoder;

    @Before
    public void setUp()
    {
        defaultJwtDecoder = new JWTDecoder(
                Algorithm.HS256, SECRET.getBytes(), gson, Arrays.asList(tokenChecker));
        noneAlgorithmJwtDecoder = new JWTDecoder(
                Algorithm.none, "".getBytes(), gson, Arrays.asList(tokenChecker));
    }

    @Test
    public void shouldFailForNullToken()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        expectedException.expectMessage("No token");
        //when
        defaultJwtDecoder.decode(null);
    }

    @Test
    public void shouldFailForEmptyToken()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        expectedException.expectMessage("No token");
        //when
        defaultJwtDecoder.decode("");
    }

    @Test
    public void shouldFailForMalformedToken()
    {
        //expect
        expectedException.expect(InvalidTokenException.class);
        expectedException.expectMessage("Invalid token structure");
        //when
        defaultJwtDecoder.decode("x.x");
    }

    @Test
    public void shouldFailForUnsupportedAlgorithm()
    {
        //expect
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("not supported");
        //given
        final String header = getTokenPart(new HashMap<String, String>()
        {
            {
                put(JWTConstants.ALGORITHM, Algorithm.HS512.name());
            }
        });
        final String payload = getTokenPart(new HashMap<>());
        //when
        defaultJwtDecoder.decode(new StringJoiner(".")
                .add(header)
                .add(payload)
                .add(encoder.encodeToString("x".getBytes()))
                .toString());
    }

    @Test
    public void shouldRecognizeInvalidSignature()
    {
        //expect
        expectedException.expect(InvalidSignatureException.class);
        expectedException.expectMessage("compromised");
        //given
        final String header = getTokenPart(new HashMap<String, String>()
        {
            {
                put(JWTConstants.ALGORITHM, Algorithm.HS256.name());
            }
        });
        final String payload = getTokenPart(new HashMap<>());
        //when
        defaultJwtDecoder.decode(new StringJoiner(".")
                .add(header)
                .add(payload)
                .add(encoder.encodeToString("x".getBytes()))
                .toString());
    }

    @Test
    public void shouldFailAtChecker() throws Exception
    {
        //expect
        expectedException.expect(ExpiredTokenException.class);
        //given
        doThrow(ExpiredTokenException.class).when(tokenChecker).check(any());
        final String header = getTokenPart(new HashMap<String, String>()
        {
            {
                put(JWTConstants.ALGORITHM, Algorithm.HS256.name());
            }
        });
        final String payload = getTokenPart(new HashMap<>());
        //when
        defaultJwtDecoder.decode(new StringJoiner(".")
                .add(header)
                .add(payload)
                .add(sign(header, payload))
                .toString());
    }

    @Test
    public void shouldReturnRegisteredClaims() throws Exception
    {
        //given
        final String header = getTokenPart(new HashMap<String, String>()
        {
            {
                put(JWTConstants.ALGORITHM, Algorithm.HS256.name());
            }
        });
        final String payload = getTokenPart(new HashMap<String, String>()
        {
            {
                put(JWTConstants.SUBJECT, SUBJECT);
                put("rubbish", "Lorem ipsum");
            }
        });
        //when
        //when
        final Map<String, String> result = defaultJwtDecoder.decode(new StringJoiner(".")
                .add(header)
                .add(payload)
                .add(sign(header, payload))
                .toString());
        //then
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(JWTConstants.SUBJECT)).isEqualTo(SUBJECT);
    }

    @Test
    public void shouldNotVerifySignatureForNoneAlgorithm()
    {
        //given
        final String header = getTokenPart(new HashMap<String, String>()
        {
            {
                put(JWTConstants.ALGORITHM, Algorithm.none.name());
            }
        });
        final String payload = getTokenPart(new HashMap<String, String>()
        {
            {
                put(JWTConstants.SUBJECT, SUBJECT);
                put("rubbish", "Lorem ipsum");
            }
        });
        //when
        //when
        final Map<String, String> result = noneAlgorithmJwtDecoder.decode(new StringJoiner(".")
                .add(header)
                .add(payload)
                .add("")
                .toString());
        //then
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(JWTConstants.SUBJECT)).isEqualTo(SUBJECT);
    }

    private String sign(String header, String payload) throws Exception
    {
        final Mac mac = Mac.getInstance(Algorithm.HS256.name);
        mac.init(new SecretKeySpec(SECRET.getBytes(), Algorithm.HS256.name));
        return new String(encoder.encodeToString(mac.doFinal(
                new StringJoiner(".").add(header).add(payload).toString().getBytes())));
    }

    private String getTokenPart(Map<String, String> parameters)
    {
        final JsonObject jsonObject = new JsonObject();
        parameters.forEach((key, value) -> jsonObject.addProperty(key, value));
        return encoder.encodeToString(gson.toJson(jsonObject).getBytes());
    }
}