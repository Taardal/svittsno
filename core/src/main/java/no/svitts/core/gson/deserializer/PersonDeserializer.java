package no.svitts.core.gson.deserializer;

import com.google.gson.*;
import no.svitts.core.date.KeyDate;
import no.svitts.core.person.Gender;
import no.svitts.core.person.Person;
import no.svitts.core.id.Id;

import java.lang.reflect.Type;

public class PersonDeserializer implements JsonDeserializer<Person> {

    @Override
    public Person deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = getId(jsonObject);
        String firstName = jsonObject.get("first_name").getAsString();
        String lastName = jsonObject.get("last_name").getAsString();
        String gender = jsonObject.get("gender").getAsString();
        String dateOfBirth = jsonObject.get("date_of_birth").getAsString();
        return new Person(id, firstName, lastName, new KeyDate(dateOfBirth), Gender.valueOf(gender));
    }

    private String getId(JsonObject jsonObject) {
        JsonElement idJsonElement = jsonObject.get("id");
        return idJsonElement != null ? idJsonElement.getAsString() : Id.get();
    }

}
