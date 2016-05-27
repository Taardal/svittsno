package no.svitts.core.gson.deserializer;

import com.google.gson.JsonElement;
import no.svitts.core.id.Id;

abstract class CoreDeserializer {

    protected String getId(JsonElement idJsonElement) {
        return !isNull(idJsonElement) ? idJsonElement.getAsString() : Id.get();
    }

    boolean isNull(JsonElement jsonElement) {
        return jsonElement == null || jsonElement.isJsonNull();
    }

    boolean isNull(String string) {
        return string == null || string.isEmpty() || string.equals("null");
    }

}
