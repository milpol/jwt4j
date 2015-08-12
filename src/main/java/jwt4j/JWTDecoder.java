package jwt4j;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jwt4j.exceptions.AlgorithmException;
import jwt4j.exceptions.InvalidSignatureException;
import jwt4j.exceptions.InvalidTokenException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JWTDecoder
{
    private final Base64.Decoder decoder;

    private final Algorithm algorithm;

    private final byte[] secret;

    private final Gson gson;

    private final List<TokenChecker> tokenCheckers;

    public JWTDecoder(final Algorithm algorithm,
                      final byte[] secret,
                      final Gson gson,
                      final List<TokenChecker> tokenCheckers)
    {
        this.decoder = Base64.getDecoder();
        this.algorithm = algorithm;
        this.secret = secret;
        this.gson = gson;
        this.tokenCheckers = Collections.unmodifiableList(tokenCheckers);
    }

    public Map<String, String> decode(final String token)
    {
        final String[] tokenParts = getTokenParts(token);

        final JsonObject headerJson = gson.fromJson(new String(decoder.decode(tokenParts[0])), JsonObject.class);
        final JsonObject payloadJson = gson.fromJson(new String(decoder.decode(tokenParts[1])), JsonObject.class);

        final Algorithm tokenAlgorithm = Algorithm.valueOf(headerJson.get(JWTConstants.ALGORITHM).getAsString());
        if (tokenAlgorithm != algorithm) {
            throw new IllegalStateException(tokenAlgorithm + " is not supported by this decoder.");
        }

        if (algorithm != Algorithm.none) {
            checkSignature(tokenParts);
        }

        tokenCheckers.forEach(checker -> checker.check(payloadJson));

        final Map<String, String> claims =
                Stream.of(JWTConstants.CLAIM_NAMES)
                        .filter(type -> payloadJson.has(type))
                        .collect(Collectors.toMap(type -> type, type -> payloadJson.get(type).getAsString()));
        return claims;
    }

    private String[] getTokenParts(String token)
    {
        if (token == null || "".equals(token)) {
            throw new InvalidTokenException("No token provided.");
        }
        final String[] tokenParts = token.split("\\" + JWTConstants.DELIMITER, -1);
        if (tokenParts.length != 3) {
            throw new InvalidTokenException("Invalid token structure.");
        }
        return tokenParts;
    }

    private void checkSignature(String[] tokenParts)
    {
        try {
            final Mac mac = Mac.getInstance(algorithm.name);
            mac.init(new SecretKeySpec(secret, algorithm.name));
            final byte[] headerAndPayload = new StringJoiner(".")
                    .add(tokenParts[0])
                    .add(tokenParts[1])
                    .toString().getBytes();
            final byte[] signature = mac.doFinal(headerAndPayload);
            if (!MessageDigest.isEqual(signature, Base64.getDecoder().decode(tokenParts[2]))) {
                throw new InvalidSignatureException("Signature has been compromised");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new AlgorithmException(e);
        }
    }
}