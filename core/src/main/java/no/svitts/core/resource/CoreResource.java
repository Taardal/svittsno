package no.svitts.core.resource;


import com.google.gson.Gson;

import javax.ws.rs.core.Response;

public abstract class CoreResource {

    protected Gson gson;

    protected CoreResource() {
        gson = new Gson();
    }

    protected Response getResponse(boolean success) {
        return success ?  Response.ok().build() : Response.serverError().build();
    }
}
