package no.svitts.core.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class HttpErrorMessage {

    private int status;
    private String message;

    public HttpErrorMessage() {
    }

    HttpErrorMessage(WebApplicationException webApplicationException) {
        status = webApplicationException.getResponse().getStatus();
        message = webApplicationException.getMessage();
    }

    public HttpErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpErrorMessage(Response.Status status, String message) {
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
