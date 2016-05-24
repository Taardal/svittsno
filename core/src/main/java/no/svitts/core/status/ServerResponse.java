package no.svitts.core.status;

public class ServerResponse {

    private Status status;
    private String message;
    private Object payload;
    private Class<?> clazz;

    ServerResponse(Status status, String message, Object payload) {
        this.status = status;
        this.message = message;
        setPayload(payload);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getPayload() {
        return clazz.cast(payload);
    }

    private void setPayload(Object payload) {
            this.payload = payload;
            clazz = payload.getClass();
    }

}
