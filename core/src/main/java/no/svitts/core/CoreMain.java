package no.svitts.core;

import no.svitts.core.server.JettyServer;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class CoreMain {

    public static void main(String[] args) throws Exception {
        JettyServer jettyServer = new JettyServer(8181);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("./core/src/main/webapp");
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        jettyServer.setHandler(resourceHandler);

        jettyServer.start();
    }
}
