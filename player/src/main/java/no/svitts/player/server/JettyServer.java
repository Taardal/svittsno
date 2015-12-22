package no.svitts.player.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class JettyServer {

    private final Server server;
    private final ServletHandler servletHandler;
    private final String address;
    private final int port;

    public JettyServer(int port) {
        server = new Server(port);
        this.port = port;
        this.address = "localhost";
        this.servletHandler = new ServletHandler();
        setHandler(servletHandler);
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return server.isRunning();
    }

    public String getStatus() {
        return isRunning() ? "RUNNING" : "OFF";
    }

    public void setHandler(Handler handler) {
        server.setHandler(handler);
    }

    public void addServlet(String servlet, String path) {
        servletHandler.addServletWithMapping(servlet, path);
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
        server.join();
    }

}
