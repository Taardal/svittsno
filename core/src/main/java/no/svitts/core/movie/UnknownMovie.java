package no.svitts.core.movie;

import no.svitts.core.date.KeyDate;
import no.svitts.core.id.Id;

import java.util.ArrayList;

public class UnknownMovie extends Movie {

    public UnknownMovie() {
        super(Id.get(), "Unknown", "Unknown","Unknown","Unknown", 0, new KeyDate(), new ArrayList<>());
    }

}
