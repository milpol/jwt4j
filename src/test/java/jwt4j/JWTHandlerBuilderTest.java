package jwt4j;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.fest.assertions.Assertions.assertThat;

public class JWTHandlerBuilderTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBuildFullStackJwtHandler()
    {
        //given
        final JWTHandlerBuilder jwtHandlerBuilder = new JWTHandlerBuilder<TestUserBean>()
                .withDataClass(TestUserBean.class)
                .withAlgorithm(Algorithm.HS512)
                .withSecret("yeah-that-is-going-to-be-super-secret".getBytes())
                .withRegisteredClaimsValidator(RegisteredClaimsValidator.DEFAULT)
                .withIssuer("Special issuer")
                .withSubject("Some subject")
                .withAudience("Limited audience")
                .withExpirationSeconds(180)
                .withNotBeforeSeconds(1)
                .withLeewaySeconds(2)
                .withIssuedAtEnabled(true)
                .withJwtIdGenerator(JWTIdGenerator.UUID_JWT_ID);
        //when
        final JWTHandler jwtHandler = jwtHandlerBuilder.build();
        final JWTEncoder jwtEncoder = jwtHandlerBuilder.buildEncoder();
        final JWTDecoder jwtDecoder = jwtHandlerBuilder.buildDecoder();
        //then
        assertThat(jwtHandler).isNotNull();
        assertThat(jwtEncoder).isNotNull();
        assertThat(jwtDecoder).isNotNull();
    }

    @Test
    public void shouldBuildJwtHandlerWithMinimumParameters()
    {
        //given
        final JWTHandlerBuilder<Object> jwtHandlerBuilder = new JWTHandlerBuilder<>()
                .withDataClass(Object.class)
                .withSecret("yeah-that-is-going-to-be-super-secret".getBytes());
        //when
        final JWTHandler jwtHandler = jwtHandlerBuilder.build();
        final JWTEncoder jwtEncoder = jwtHandlerBuilder.buildEncoder();
        final JWTDecoder jwtDecoder = jwtHandlerBuilder.buildDecoder();
        //then
        assertThat(jwtHandler).isNotNull();
        assertThat(jwtEncoder).isNotNull();
        assertThat(jwtDecoder).isNotNull();
    }

    @Test
    public void shouldFailForMissingSecretParameter()
    {
        //expect
        expectedException.expect(IllegalArgumentException.class);
        //given
        final JWTHandlerBuilder<Object> jwtHandlerBuilder = new JWTHandlerBuilder<>();
        //when
        jwtHandlerBuilder.build();
    }

    @Test
    public void shouldNotFailForMissingSecretWhenNoAlgorithmDefined()
    {
        //given
        final JWTHandlerBuilder<Object> jwtHandlerBuilder = new JWTHandlerBuilder<>()
                .withDataClass(Object.class)
                .withAlgorithm(Algorithm.none);
        //when
        final JWTHandler jwtHandler = jwtHandlerBuilder.build();
        final JWTEncoder jwtEncoder = jwtHandlerBuilder.buildEncoder();
        final JWTDecoder jwtDecoder = jwtHandlerBuilder.buildDecoder();
        //then
        assertThat(jwtHandler).isNotNull();
        assertThat(jwtEncoder).isNotNull();
        assertThat(jwtDecoder).isNotNull();
    }

    @Test
    public void shouldFailForMissingDataTypeParameter()
    {
        //expect
        expectedException.expect(IllegalArgumentException.class);
        //given
        final JWTHandlerBuilder<Object> jwtHandlerBuilder = new JWTHandlerBuilder<>()
                .withSecret("yeah-that-is-going-to-be-super-secret".getBytes());
        //when
        jwtHandlerBuilder.build();
    }

    @Test
    public void shouldFailForMissingRegisteredClaimsValidator()
    {
        //expect
        expectedException.expect(IllegalArgumentException.class);
        //given
        final JWTHandlerBuilder<Object> jwtHandlerBuilder = new JWTHandlerBuilder<>()
                .withSecret("yeah-that-is-going-to-be-super-secret".getBytes())
                .withRegisteredClaimsValidator(null);
        //when
        jwtHandlerBuilder.build();
    }

    @Test
    public void shouldFailForMissingAlgorithmParameters()
    {
        //expect
        expectedException.expect(IllegalArgumentException.class);
        //given
        final JWTHandlerBuilder<Object> jwtHandlerBuilder = new JWTHandlerBuilder<>()
                .withSecret("yeah-that-is-going-to-be-super-secret".getBytes())
                .withAlgorithm(null);
        //when
        jwtHandlerBuilder.build();
    }
}