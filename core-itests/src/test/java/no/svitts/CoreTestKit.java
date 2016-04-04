package no.svitts;

import com.google.gson.Gson;
import no.svitts.client.CoreClient;

public class CoreTestKit {

    protected CoreClient coreClient;
    protected Gson gson;

    protected CoreTestKit() {
        coreClient = new CoreClient();
        gson = new Gson();
    }

    protected <T> String getJson(T object, Class<T> clazz) {
        return gson.toJson(object, clazz);
    }

}
