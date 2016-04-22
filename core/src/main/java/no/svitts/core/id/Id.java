package no.svitts.core.id;

import java.util.UUID;

public class Id {

    private Id() {
    }

    public static String get() {
        return UUID.randomUUID().toString();
    }

}
