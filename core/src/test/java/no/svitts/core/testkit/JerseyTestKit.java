package no.svitts.core.testkit;

import no.svitts.core.resource.CoreResource;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyTestKit {

    private JerseyTestKit() {
    }

    public static <T extends CoreResource> ResourceConfig getResourceConfig(T resource) {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(resource);
        return resourceConfig;
    }

}
