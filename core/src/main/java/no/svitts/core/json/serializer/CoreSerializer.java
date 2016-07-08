package no.svitts.core.json.serializer;

import com.google.gson.JsonPrimitive;

abstract class CoreSerializer {

    JsonPrimitive getJsonPrimitive(String string) {
        return string != null ? new JsonPrimitive(string) : null;
    }

    JsonPrimitive getJsonPrimitive(int number) {
        return new JsonPrimitive(number);
    }

    JsonPrimitive getJsonPrimitive(long number) {
        return new JsonPrimitive(number);
    }

}
