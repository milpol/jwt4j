package jwt4j;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class UUIDJWTIdGeneratorTest
{
    @Test
    public void test()
    {
        //given
        final UUIDJWTIdGenerator uuidjwtIdGenerator = new UUIDJWTIdGenerator();
        //when
        final String jwtId = uuidjwtIdGenerator.getJwtId();
        //then
        assertThat(jwtId).isNotEmpty();
    }
}