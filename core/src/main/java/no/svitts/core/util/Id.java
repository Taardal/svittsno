package no.svitts.core.util;

import java.util.UUID;

public class Id {

    public static String get() {
        return UUID.randomUUID().toString();
    }
}
