package no.svitts.core.gson.deserializer;

import com.google.gson.JsonElement;

abstract class CoreDeserializer {

    boolean isNotNull(JsonElement jsonElement) {
        return jsonElement != null && !jsonElement.isJsonNull();
    }

}
