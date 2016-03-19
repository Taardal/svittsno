package no.svitts.core.resource;


import javax.ws.rs.core.Response;

public abstract class SvittsResource {

    protected Response getRespone(boolean success) {
        return success ? Response.ok().build() : Response.serverError().build();
    }
}
