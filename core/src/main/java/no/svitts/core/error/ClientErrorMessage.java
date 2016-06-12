package no.svitts.core.error;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

public class ClientErrorMessage {

    private int status;
    private List<String> messages;

    public ClientErrorMessage(int status, List<String> messages) {
        this.status = status;
        this.messages = messages;
    }

    public ClientErrorMessage(Response.Status status, List<String> messages) {
        this.status = status.getStatusCode();
        this.messages = messages;
    }

    public ClientErrorMessage(int status, String... messages) {
        this.status = status;
        this.messages = Arrays.asList(messages);
    }

    public ClientErrorMessage(Response.Status status, String... messages) {
        this.status = status.getStatusCode();
        this.messages = Arrays.asList(messages);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

}
