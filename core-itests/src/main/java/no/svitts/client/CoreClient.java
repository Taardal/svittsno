package no.svitts.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class CoreClient {

    private static final String CORE_API_ADDRESS = "http://localhost:8080/api";
    private Client client;

    public CoreClient() {
        client = ClientBuilder.newClient();
    }

    public RequestBuilder request() {
        return new RequestBuilder(client.target(CORE_API_ADDRESS), MediaType.APPLICATION_JSON_TYPE);
    }

}
