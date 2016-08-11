package no.svitts.core.json.deserializer;

import com.google.gson.JsonElement;

abstract class Deserializer {

    String getString(JsonElement jsonElement) {
        return isNotNull(jsonElement) && !jsonElement.getAsString().equals("null") ? jsonElement.getAsString() : null;
    }

    int getInt(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? jsonElement.getAsInt() : 0;
    }

    long getLong(JsonElement jsonElement) {
        return isNotNull(jsonElement) ? jsonElement.getAsLong() : 0;
    }

    boolean isNotNull(JsonElement jsonElement) {
        return jsonElement != null && !jsonElement.isJsonNull();
    }

}
