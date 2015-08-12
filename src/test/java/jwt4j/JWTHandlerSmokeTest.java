package jwt4j;

import org.junit.Test;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;

public class JWTHandlerSmokeTest
{
    private JWTHandler<String> stringJWTHandler = new JWTHandlerBuilder<>()
            .withSecret("super-secret".getBytes())
            .withDataClass(String.class)
            .withIssuer("issuer")
            .withExpirationSeconds(10)
            .build();

    private JWTHandler<String> stringNoneJWTHandler = new JWTHandlerBuilder<>()
            .withAlgorithm(Algorithm.none)
            .withDataClass(String.class)
            .withIssuer("issuer")
            .withExpirationSeconds(10)
            .build();

    private JWTHandler<TestUserBean> completeJWTHandler = new JWTHandlerBuilder<>()
            .withAlgorithm(Algorithm.HS512)
            .withSecret("super-secret".getBytes())
            .withDataClass(TestUserBean.class)
            .withIssuer("issuer")
            .withSubject("subject")
            .withAudience("audience")
            .withExpirationSeconds(10)
            .withNotBeforeSeconds(2)
            .withLeewaySeconds(1)
            .withJwtIdGenerator(JWTIdGenerator.UUID_JWT_ID)
            .withIssuedAtEnabled(true)
            .build();


    @Test
    public void simpleRoundTest() throws InterruptedException
    {
        //given
        final String data = "test";
        //when
        final String token = stringJWTHandler.encode(data);
        System.out.println("Token: " + token);
        final Payload<String> decodedPayload = stringJWTHandler.decodeToPayload(token);
        System.out.println(decodedPayload);
        //then
        assertThat(decodedPayload.getData()).isEqualTo(data);
    }

    @Test
    public void simpleRoundTestWithNoneAlgorithm()
    {
        //given
        final String data = "test";
        //when
        final String token = stringNoneJWTHandler.encode(data);
        System.out.println("Token: " + token);
        final Payload<String> decodedPayload = stringNoneJWTHandler.decodeToPayload(token);
        System.out.println(decodedPayload);
        //then
        assertThat(decodedPayload.getData()).isEqualTo(data);
    }

    @Test
    public void simpleRoundTestWithCompleteParameters() throws InterruptedException
    {
        //given
        final TestUserBean sgPepper = new TestUserBean();
        sgPepper.setUsername("Sg.Pepper");
        sgPepper.setPassword("show".getBytes());
        final TestUserBean sgPepperFriend = new TestUserBean();
        sgPepperFriend.setUsername("Paul");
        sgPepper.setFriends(Arrays.asList(sgPepperFriend));
        //when
        final String token = completeJWTHandler.encode(sgPepper);
        System.out.println("Token: " + token);
        Thread.sleep(1000);
        final Payload<TestUserBean> decodedPayload = completeJWTHandler.decodeToPayload(token);
        System.out.println(decodedPayload);
        //then
        assertThat(decodedPayload.getData()).isEqualTo(sgPepper);
    }
}