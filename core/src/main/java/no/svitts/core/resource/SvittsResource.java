package no.svitts.core.resource;


import com.google.gson.Gson;

import javax.ws.rs.core.Response;

public abstract class SvittsResource {

    protected Gson gson;

    protected SvittsResource() {
        gson = new Gson();
    }

    protected Response getResponse(boolean success) {
        if (success) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }
}
