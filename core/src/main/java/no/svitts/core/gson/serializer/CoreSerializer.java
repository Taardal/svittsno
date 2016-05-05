package no.svitts.core.gson.serializer;

import com.google.gson.JsonPrimitive;
import no.svitts.core.date.KeyDate;

public abstract class CoreSerializer {

    protected JsonPrimitive getJsonPrimitive(String string) {
        return string != null ? new JsonPrimitive(string) : new JsonPrimitive("null");
    }

    protected JsonPrimitive getJsonPrimitive(int number) {
        return number >= 0 ? new JsonPrimitive(number) : new JsonPrimitive(0);
    }

    protected JsonPrimitive getJsonPrimitive(long number) {
        return number >= 0 ? new JsonPrimitive(number) : new JsonPrimitive(0);
    }

    protected JsonPrimitive getJsonPrimitive(KeyDate keyDate) {
        return keyDate != null ? getJsonPrimitive(keyDate.toString()) : new JsonPrimitive("null");
    }

}
