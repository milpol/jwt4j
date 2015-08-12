package jwt4j;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JWTHandlerTest
{
    private static final String DATA = "Data";
    private static final String SUBJECT = "Subject";
    private static final String TOKEN = "token";
    private static final String ISSUER = "Issuer";
    private static final String AUDIENCE = "Audience";

    private JWTHandler<String> jwtHandler;

    @Mock
    private JWTEncoder jwtEncoder;

    @Mock
    private JWTDecoder jwtDecoder;

    @Before
    public void setUp()
    {
        jwtHandler = new JWTHandler<>(
                jwtEncoder,
                jwtDecoder,
                String.class,
                new Gson(),
                Optional.of(ISSUER),
                Optional.of(SUBJECT),
                Optional.of(AUDIENCE),
                180,
                30,
                true,
                Optional.of(JWTIdGenerator.UUID_JWT_ID)
        );
    }

    @Test
    public void shouldEncodeData()
    {
        //given
        given(jwtEncoder.encode(Matchers.anyMap())).willReturn(TOKEN);
        //when
        final String token = jwtHandler.encode(DATA);
        //then
        assertThat(token).isEqualTo(TOKEN);
        verify(jwtEncoder, times(1)).encode(Matchers.argThat(new ArgumentMatcher<Map<String, String>>()
        {
            @Override
            public boolean matches(Object input)
            {
                final Map<String, String> inputArgument = (Map<String, String>) input;
                return inputArgument.containsKey(JWTConstants.ISSUER) &&
                        inputArgument.get(JWTConstants.ISSUER).equals(ISSUER) &&
                        inputArgument.containsKey(JWTConstants.SUBJECT) &&
                        inputArgument.get(JWTConstants.SUBJECT).equals(SUBJECT) &&
                        inputArgument.containsKey(JWTConstants.AUDIENCE) &&
                        inputArgument.get(JWTConstants.AUDIENCE).equals(AUDIENCE) &&
                        inputArgument.containsKey(JWTConstants.EXPIRATION) &&
                        Long.valueOf(inputArgument.get(JWTConstants.EXPIRATION)) > 0 &&
                        inputArgument.containsKey(JWTConstants.NOT_BEFORE) &&
                        Long.valueOf(inputArgument.get(JWTConstants.NOT_BEFORE)) > 0 &&
                        inputArgument.containsKey(JWTConstants.JWT_ID) &&
                        inputArgument.containsKey(JWTConstants.DATA);
            }
        }));
    }

    @Test
    public void shouldDecodeTokenToPayload()
    {
        //given
        given(jwtDecoder.decode(TOKEN)).willReturn(new HashMap<String, String>()
        {
            {
                put(JWTConstants.SUBJECT, SUBJECT);
                put(JWTConstants.DATA, "\"" + DATA + "\"");
            }
        });
        //when
        final Payload<String> payload = jwtHandler.decodeToPayload(TOKEN);
        //then
        assertThat(payload).isEqualTo(new Payload<>(DATA, new Claims(new HashMap<String, String>()
        {
            {
                put(JWTConstants.SUBJECT, SUBJECT);
            }
        })));
    }

    @Test
    public void shouldDecodeTokenToData()
    {
        //given
        given(jwtDecoder.decode(TOKEN)).willReturn(new HashMap<String, String>()
        {
            {
                put(JWTConstants.SUBJECT, SUBJECT);
                put(JWTConstants.DATA, "\"" + DATA + "\"");
            }
        });
        //when
        final String data = jwtHandler.decode(TOKEN);
        //then
        assertThat(data).isEqualTo(DATA);

    }
}