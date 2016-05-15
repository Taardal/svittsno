package no.svitts.core.gson.serializer;

import com.google.gson.JsonPrimitive;
import no.svitts.core.date.KeyDate;

abstract class CoreSerializer {

    JsonPrimitive getJsonPrimitive(String string) {
        return string != null ? new JsonPrimitive(string) : getJsonPrimitiveNullString();
    }

    JsonPrimitive getJsonPrimitive(int number) {
        return number >= 0 ? new JsonPrimitive(number) : new JsonPrimitive(0);
    }

    JsonPrimitive getJsonPrimitive(long number) {
        return number >= 0 ? new JsonPrimitive(number) : new JsonPrimitive(0);
    }

    JsonPrimitive getJsonPrimitive(KeyDate keyDate) {
        return keyDate != null ? getJsonPrimitive(keyDate.toString()) : getJsonPrimitiveNullString();
    }

    private JsonPrimitive getJsonPrimitiveNullString() {
        return new JsonPrimitive("null");
    }

}
