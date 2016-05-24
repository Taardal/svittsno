package no.svitts.core.status;

import no.svitts.core.builder.Builder;

public class ServerResponseBuilder implements Builder<ServerResponse> {

    private Status status;
    private String message;
    private Object payload;

    public ServerResponseBuilder() {
        status = null;
        message = "";
        payload = null;
    }

    @Override
    public ServerResponse build() {
        return new ServerResponse(status, message, payload);
    }

    public ServerResponseBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public ServerResponseBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ServerResponseBuilder payload(Object payload) {
        this.payload = payload;
        return this;
    }

    public ServerResponseBuilder ok() {
        status = Status.OK;
        message(status.getMessage());
        return this;
    }

    public ServerResponseBuilder failure() {
        status = Status.FAILURE;
        message(status.getMessage());
        return this;
    }
}
