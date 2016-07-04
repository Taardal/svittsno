package no.svitts.core.json.serializer;

import com.google.gson.JsonPrimitive;
import no.svitts.core.date.ReleaseDate;

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

    JsonPrimitive getJsonPrimitive(ReleaseDate releaseDate) {
        return releaseDate != null ? getJsonPrimitive(releaseDate.toString()) : getJsonPrimitiveNullString();
    }

    private JsonPrimitive getJsonPrimitiveNullString() {
        return new JsonPrimitive("null");
    }

}
