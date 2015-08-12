package jwt4j;

import com.google.gson.JsonObject;

public interface TokenChecker
{
    void check(JsonObject payloadJson);
}
