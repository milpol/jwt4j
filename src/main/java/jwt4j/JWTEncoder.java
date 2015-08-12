package jwt4j;

import com.google.gson.JsonObject;
import jwt4j.exceptions.AlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.StringJoiner;

public class JWTEncoder
{
    private final Algorithm algorithm;

    private final byte[] secret;

    private final String base64JsonHeader;

    private final RegisteredClaimsValidator registeredClaimsValidator;

    public JWTEncoder(final Algorithm algorithm,
                      final byte[] secret,
                      final RegisteredClaimsValidator registeredClaimsValidator)
    {
        this.algorithm = algorithm;
        this.secret = secret;
        this.base64JsonHeader = encodeHeader();
        this.registeredClaimsValidator = registeredClaimsValidator;
    }

    private String encodeHeader()
    {
        final JsonObject headerJsonObject = new JsonObject();
        headerJsonObject.addProperty(JWTConstants.TYPE, JWTConstants.JWT);
        headerJsonObject.addProperty(JWTConstants.ALGORITHM, algorithm.name());
        return toBase64Json(headerJsonObject);
    }

    public String encode(final Map<String, String> claims)
    {
        registeredClaimsValidator.validate(claims);

        final String base64JsonPayload = encodePayload(claims);
        final StringJoiner stringJoiner = new StringJoiner(JWTConstants.DELIMITER)
                .add(base64JsonHeader)
                .add(base64JsonPayload);
        if (algorithm != Algorithm.none) {
            stringJoiner.add(sign(stringJoiner.toString()));
        } else {
            stringJoiner.add("");
        }
        return stringJoiner.toString();
    }

    private String encodePayload(final Map<String, String> claims)
    {
        final JsonObject payloadJsonObject = new JsonObject();
        claims.forEach((claim, value) -> {
            payloadJsonObject.addProperty(claim, value);
        });
        return toBase64Json(payloadJsonObject);
    }

    private String sign(final String message)
    {
        try {
            final Mac mac = Mac.getInstance(algorithm.name);
            mac.init(new SecretKeySpec(secret, algorithm.name));
            final String signature = new String(Base64.getEncoder().encodeToString(mac.doFinal(message.getBytes())));
            return signature;
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalArgumentException e) {
            throw new AlgorithmException(e);
        }
    }

    private String toBase64Json(final JsonObject jsonObject)
    {
        final String base64ObjectJson = Base64.getEncoder()
                .encodeToString(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        return base64ObjectJson;
    }
}