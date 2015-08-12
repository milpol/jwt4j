package jwt4j;

import jwt4j.exceptions.AlgorithmException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Base64;
import java.util.HashMap;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JWTEncoderTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private RegisteredClaimsValidator registeredClaimsValidator;

    @Test
    public void shouldCreateNoneSignedToken()
    {
        //given
        final JWTEncoder jwtEncoder = new JWTEncoder(Algorithm.none, "".getBytes(), registeredClaimsValidator);
        final HashMap<String, String> claims = new HashMap<>();
        claims.put("claimName", "claimValue");
        //when
        final String token = jwtEncoder.encode(claims);
        final String[] tokenParts = token.split("\\" + JWTConstants.DELIMITER);
        //then
        final String header = new String(Base64.getDecoder().decode(tokenParts[0]));
        final String payload = new String(Base64.getDecoder().decode(tokenParts[1]));
        assertThat(tokenParts).isNotNull().hasSize(2);
        assertThat(header).isEqualTo("{\"typ\":\"JWT\",\"alg\":\"none\"}");
        assertThat(payload).isEqualTo("{\"claimName\":\"claimValue\"}");
    }

    @Test
    public void shouldCreateSignedToken()
    {
        //given
        final JWTEncoder jwtEncoder = new JWTEncoder(Algorithm.HS256, "secret".getBytes(), registeredClaimsValidator);
        final HashMap<String, String> claims = new HashMap<>();
        claims.put("claimName", "claimValue");
        //when
        final String token = jwtEncoder.encode(claims);
        final String[] tokenParts = token.split("\\" + JWTConstants.DELIMITER);
        //then
        final String header = new String(Base64.getDecoder().decode(tokenParts[0]));
        final String payload = new String(Base64.getDecoder().decode(tokenParts[1]));
        assertThat(tokenParts).isNotNull().hasSize(3);
        assertThat(header).isEqualTo("{\"typ\":\"JWT\",\"alg\":\"HS256\"}");
        assertThat(payload).isEqualTo("{\"claimName\":\"claimValue\"}");
        assertThat(tokenParts[2]).isNotEmpty();
    }

    @Test
    public void shouldFailForInvalidKey()
    {
        //expect
        expectedException.expect(AlgorithmException.class);
        //given
        final JWTEncoder jwtEncoder = new JWTEncoder(Algorithm.HS256, "".getBytes(), registeredClaimsValidator);
        final HashMap<String, String> claims = new HashMap<>();
        claims.put("claimName", "claimValue");
        //when
        jwtEncoder.encode(claims);
    }
}