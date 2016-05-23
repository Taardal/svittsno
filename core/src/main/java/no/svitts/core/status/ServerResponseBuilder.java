package no.svitts.core.status;

import no.svitts.core.builder.Builder;

public class ServerResponseBuilder<T> implements Builder<ServerResponse<T>> {

    private Status status;
    private String message;
    private T payload;

    public ServerResponseBuilder() {
        status = null;
        message = "";
        payload = null;
    }

    @Override
    public ServerResponse<T> build() {
        return new ServerResponse<>(status, message, payload);
    }

    public ServerResponseBuilder<T> status(Status status) {
        this.status = status;
        return this;
    }

    public ServerResponseBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    public ServerResponseBuilder<T> payload(T payload) {
        this.payload = payload;
        return this;
    }

    public ServerResponseBuilder<T> ok() {
        status = Status.OK;
        message(status.getMessage());
        return this;
    }

    public ServerResponseBuilder<T> failure() {
        status = Status.FAILURE;
        message(status.getMessage());
        return this;
    }

    public ServerResponseBuilder<T> notFound() {
        status = Status.NOT_FOUND;
        message(status.getMessage());
        return this;
    }
}
