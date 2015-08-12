package jwt4j;

import com.google.gson.Gson;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JWTHandler<T>
{
    private final Class<T> dataClass;

    private final Gson gson;

    private final Map<String, String> registeredStaticClaims;

    private final int expirationSeconds;

    private final int notBeforeSeconds;

    private final boolean issuedAtEnabled;

    private final Optional<JWTIdGenerator> jwtIdGenerator;

    private final JWTEncoder JWTEncoder;

    private final JWTDecoder JWTDecoder;

    JWTHandler(final JWTEncoder JWTEncoder,
               final JWTDecoder JWTDecoder,
               final Class<T> dataClass,
               final Gson gson,
               final Optional<String> issuer,
               final Optional<String> subject,
               final Optional<String> audience,
               final int expirationSeconds,
               final int notBeforeSeconds,
               final boolean issuedAtEnabled,
               final Optional<JWTIdGenerator> jwtIdGenerator)
    {
        this.JWTEncoder = JWTEncoder;
        this.JWTDecoder = JWTDecoder;
        this.dataClass = dataClass;
        this.gson = gson;
        this.registeredStaticClaims = getRegisteredStaticClaims(issuer, subject, audience);
        this.expirationSeconds = expirationSeconds;
        this.notBeforeSeconds = notBeforeSeconds;
        this.issuedAtEnabled = issuedAtEnabled;
        this.jwtIdGenerator = jwtIdGenerator;
    }

    private Map<String, String> getRegisteredStaticClaims(final Optional<String> issuer,
                                                          final Optional<String> subject,
                                                          final Optional<String> audience)
    {
        Map<String, String> registeredClaims = new HashMap<>();
        if (issuer.isPresent()) {
            registeredClaims.put(JWTConstants.ISSUER, issuer.get());
        }
        if (subject.isPresent()) {
            registeredClaims.put(JWTConstants.SUBJECT, subject.get());
        }
        if (audience.isPresent()) {
            registeredClaims.put(JWTConstants.AUDIENCE, audience.get());
        }
        return Collections.unmodifiableMap(registeredClaims);
    }

    private Map<String, String> getRegisteredClaims()
    {
        final Map<String, String> variableRegisteredClaims = new HashMap<>(registeredStaticClaims);
        if (expirationSeconds > 0) {
            variableRegisteredClaims.put(JWTConstants.EXPIRATION,
                    String.valueOf(Instant.now().plusSeconds(expirationSeconds).getEpochSecond()));
        }
        if (notBeforeSeconds > 0) {
            variableRegisteredClaims.put(JWTConstants.NOT_BEFORE,
                    String.valueOf(Instant.now().plusSeconds(notBeforeSeconds).getEpochSecond()));
        }
        if (issuedAtEnabled) {
            variableRegisteredClaims.put(JWTConstants.ISSUED_AT,
                    String.valueOf(Instant.now().getEpochSecond()));
        }
        if (jwtIdGenerator.isPresent()) {
            variableRegisteredClaims.put(JWTConstants.JWT_ID, jwtIdGenerator.get().getJwtId());
        }
        return variableRegisteredClaims;
    }

    public String encode(final T data)
    {
        final Map<String, String> claims = new HashMap<>(getRegisteredClaims());
        claims.put(JWTConstants.DATA, gson.toJsonTree(data).toString());
        return JWTEncoder.encode(claims);
    }

    public T decode(final String token)
    {
        return decodeToPayload(token).getData();
    }

    public Payload<T> decodeToPayload(String token)
    {
        final Map<String, String> claims = JWTDecoder.decode(token);
        T data = gson.fromJson(claims.remove(JWTConstants.DATA), dataClass);
        return new Payload<>(data, new Claims(claims));
    }
}