package no.svitts.core.status;

public enum Status {

    OK("Completed successfully."), FAILURE("An error occurred.");

    private String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
