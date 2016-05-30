package no.svitts.core.error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ErrorMessage {

    private int status;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(WebApplicationException webApplicationException) {
        status = webApplicationException.getResponse().getStatus();
        message = webApplicationException.getMessage();
    }

    public ErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorMessage(Response.Status status, String message) {
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
