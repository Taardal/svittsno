package no.svitts.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class RequestBuilder {

    private WebTarget webTarget;
    private MediaType mediaType;

    public RequestBuilder(WebTarget webTarget, MediaType mediaType) {
        this.webTarget = webTarget;
        this.mediaType = mediaType;
    }

    public RequestBuilder path(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        webTarget.path(path);
        return this;
    }

    public RequestBuilder query(String key, String value) {
        webTarget.queryParam(key, value);
        return this;
    }

    public Request get() {
        return new Request(requestMediaType().buildGet());
    }

    public Request post(String payload) {
        return new Request(requestMediaType().buildPost(getEntity(payload)));
    }

    public Request put(String payload) {
        return new Request(requestMediaType().buildPut(getEntity(payload)));
    }

    public Request delete() {
        return new Request(requestMediaType().buildDelete());
    }

    private Invocation.Builder requestMediaType() {
        return webTarget.request(mediaType);
    }

    private Entity<String> getEntity(String payload) {
        return Entity.entity(payload, mediaType);
    }
}
