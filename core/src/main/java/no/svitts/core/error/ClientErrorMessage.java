package no.svitts.core.error;

import javax.ws.rs.core.Response;

public class ClientErrorMessage {

    private int status;
    private String message;

    public ClientErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ClientErrorMessage(Response.Status status, String message) {
        this.status = status.getStatusCode();
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
