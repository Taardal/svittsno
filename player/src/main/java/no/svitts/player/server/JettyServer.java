package no.svitts.player.server;

import javax.servlet.http.HttpServlet;

public interface JettyServer {

    void start();
    void stop();
    boolean isRunning();
    String getUrl();
    void addServlet(HttpServlet httpServlet, String mapping);

}
