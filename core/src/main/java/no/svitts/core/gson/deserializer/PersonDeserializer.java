package no.svitts.core.gson.deserializer;

import com.google.gson.*;
import no.svitts.core.person.Gender;
import no.svitts.core.person.Person;
import no.svitts.core.util.Id;

import java.lang.reflect.Type;
import java.sql.Date;

public class PersonDeserializer implements JsonDeserializer<Person> {

    @Override
    public Person deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = getId(jsonObject);
        String name = jsonObject.get("name").getAsString();
        String gender = jsonObject.get("gender").getAsString();
        String dateOfBirth = jsonObject.get("date_of_birth").getAsString();
        return new Person(id, name, Date.valueOf(dateOfBirth), Gender.valueOf(gender));
    }

    private String getId(JsonObject jsonObject) {
        JsonElement idJsonElement = jsonObject.get("id");
        return idJsonElement != null ? idJsonElement.getAsString() : Id.get();
    }

}
