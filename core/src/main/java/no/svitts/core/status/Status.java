package no.svitts.core.status;

public enum Status {

    OK("Completed successfully."), FAILURE("An error occurred."), NOT_FOUND("Could not find target(s) in database");

    private String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
