package no.svitts.core.status;

public class ServerResponse<T> {

    private Status status;
    private String message;
    private T payload;

    ServerResponse(Status status, String message, T payload) {
        this.status = status;
        this.message = message;
        this.payload = payload;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getPayload() {
        return payload;
    }

    public boolean containsPayload() {
        return payload != null;
    }

}
