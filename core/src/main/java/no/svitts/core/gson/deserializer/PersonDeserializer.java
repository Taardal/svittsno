package no.svitts.core.gson.deserializer;

import com.google.gson.*;
import no.svitts.core.person.Gender;
import no.svitts.core.person.Person;

import java.lang.reflect.Type;
import java.util.UUID;

public class PersonDeserializer implements JsonDeserializer<Person> {

    @Override
    public Person deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = getId(jsonObject);
        String name = jsonObject.get("name").getAsString();
        String gender = jsonObject.get("gender").getAsString();
        int age = jsonObject.get("age").getAsInt();
        return new Person(id, name, age, Gender.valueOf(gender));
    }

    private String getId(JsonObject jsonObject) {
        JsonElement idJsonElement = jsonObject.get("id");
        return idJsonElement != null ? idJsonElement.getAsString() : UUID.randomUUID().toString();
    }

}
