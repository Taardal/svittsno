package no.svitts.client;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

public class Request {

    private Invocation invocation;

    public Request(Invocation invocation) {
        this.invocation = invocation;
    }

    public Request property(String key, String value) {
        invocation.property(key, value);
        return this;
    }

    public Response response() {
        return invocation.invoke();
    }
}
