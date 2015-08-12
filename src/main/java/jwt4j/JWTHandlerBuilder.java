package jwt4j;

import com.google.gson.Gson;
import jwt4j.checkers.AudienceChecker;
import jwt4j.checkers.ExpirationChecker;
import jwt4j.checkers.ExpirationIssuedAtChecker;
import jwt4j.checkers.IdChecker;
import jwt4j.checkers.IssuerChecker;
import jwt4j.checkers.NotBeforeChecker;
import jwt4j.checkers.SubjectChecker;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class JWTHandlerBuilder<T>
{
    private final Gson gson = new Gson();

    private Optional<Class<T>> dataClass = Optional.empty();

    private Optional<Algorithm> algorithm = Optional.of(Algorithm.HS256);

    private Optional<byte[]> secret = Optional.empty();

    private Optional<RegisteredClaimsValidator> registeredClaimsValidator = Optional.of(RegisteredClaimsValidator.DEFAULT);

    private Optional<String> issuer = Optional.empty();

    private Optional<String> subject = Optional.empty();

    private Optional<String> audience = Optional.empty();

    private int expirationSeconds = -1;

    private int notBeforeSeconds = -1;

    private int leewaySeconds = 0;

    private boolean issuedAtEnabled = false;

    private Optional<JWTIdGenerator> jwtIdGenerator = Optional.empty();

    public JWTHandlerBuilder withDataClass(Class<T> dataClass)
    {
        this.dataClass = Optional.ofNullable(dataClass);
        return this;
    }

    public JWTHandlerBuilder withAlgorithm(Algorithm algorithm)
    {
        this.algorithm = Optional.ofNullable(algorithm);
        if (algorithm == Algorithm.none) {
            secret = Optional.of("".getBytes());
        }
        return this;
    }

    public JWTHandlerBuilder withSecret(byte[] secret)
    {
        this.secret = Optional.ofNullable(secret);
        return this;
    }

    public JWTHandlerBuilder withRegisteredClaimsValidator(RegisteredClaimsValidator registeredClaimsValidator)
    {
        this.registeredClaimsValidator = Optional.ofNullable(registeredClaimsValidator);
        return this;
    }

    public JWTHandlerBuilder withIssuer(String issuer)
    {
        this.issuer = Optional.ofNullable(issuer);
        return this;
    }

    public JWTHandlerBuilder withSubject(String subject)
    {
        this.subject = Optional.ofNullable(subject);
        return this;
    }

    public JWTHandlerBuilder withAudience(String audience)
    {
        this.audience = Optional.ofNullable(audience);
        return this;
    }

    public JWTHandlerBuilder withExpirationSeconds(int expirationSeconds)
    {
        this.expirationSeconds = expirationSeconds;
        return this;
    }

    public JWTHandlerBuilder withNotBeforeSeconds(int notBeforeSeconds)
    {
        this.notBeforeSeconds = notBeforeSeconds;
        return this;
    }

    public JWTHandlerBuilder withLeewaySeconds(int leewaySeconds)
    {
        this.leewaySeconds = leewaySeconds;
        return this;
    }

    public JWTHandlerBuilder withIssuedAtEnabled(boolean issuedAtEnabled)
    {
        this.issuedAtEnabled = issuedAtEnabled;
        return this;
    }

    public JWTHandlerBuilder withJwtIdGenerator(JWTIdGenerator jwtIdGenerator)
    {
        this.jwtIdGenerator = Optional.ofNullable(jwtIdGenerator);
        return this;
    }

    public JWTHandler<T> build()
    {
        return new JWTHandler<>(
                buildEncoder(),
                buildDecoder(),
                dataClass.orElseThrow(IllegalArgumentException::new),
                gson,
                issuer,
                subject,
                audience,
                expirationSeconds,
                notBeforeSeconds,
                issuedAtEnabled,
                jwtIdGenerator);
    }

    public JWTDecoder buildDecoder()
    {
        return new JWTDecoder(
                algorithm.orElseThrow(IllegalArgumentException::new),
                secret.orElseThrow(IllegalArgumentException::new),
                gson,
                buildTokenCheckers());
    }

    private List<TokenChecker> buildTokenCheckers()
    {
        final LinkedList<TokenChecker> tokenCheckers = new LinkedList<>();

        if (expirationSeconds > 0) {
            if (issuedAtEnabled) {
                tokenCheckers.add(new ExpirationIssuedAtChecker(expirationSeconds, leewaySeconds));
            } else {
                tokenCheckers.add(new ExpirationChecker(leewaySeconds));
            }
        }

        if (notBeforeSeconds > 0) {
            tokenCheckers.add(new NotBeforeChecker(leewaySeconds));
        }

        if (issuer.isPresent()) {
            tokenCheckers.add(new IssuerChecker(issuer.get()));
        }

        if (audience.isPresent()) {
            tokenCheckers.add(new AudienceChecker(audience.get()));
        }

        if (subject.isPresent()) {
            tokenCheckers.add(new SubjectChecker(subject.get()));
        }

        if (jwtIdGenerator.isPresent()) {
            tokenCheckers.add(new IdChecker());
        }

        return tokenCheckers;
    }

    public JWTEncoder buildEncoder()
    {
        return new JWTEncoder(
                algorithm.orElseThrow(IllegalArgumentException::new),
                secret.orElseThrow(IllegalArgumentException::new),
                registeredClaimsValidator.orElseThrow(IllegalArgumentException::new));
    }
}