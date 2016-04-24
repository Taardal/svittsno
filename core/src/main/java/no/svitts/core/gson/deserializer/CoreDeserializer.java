package no.svitts.core.gson.deserializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.svitts.core.id.Id;

public abstract class CoreDeserializer {

    protected String getId(JsonObject jsonObject) {
        JsonElement idJsonElement = jsonObject.get("id");
        return idJsonElement != null ? idJsonElement.getAsString() : Id.get();
    }

}
