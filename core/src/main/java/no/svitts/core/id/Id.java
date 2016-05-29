package no.svitts.core.id;

import java.util.UUID;

public class Id {

    public static final int MAX_LENGTH = 255;

    private Id() {
    }

    public static String get() {
        return UUID.randomUUID().toString();
    }

}
