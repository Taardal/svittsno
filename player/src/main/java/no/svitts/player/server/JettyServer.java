package no.svitts.player.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;

public class JettyServer {

    private static final String RUNNING = "RUNNING";
    private static final String OFF = "OFF";
    private static final Logger LOGGER = LoggerFactory.getLogger(JettyServer.class);

    private final Server server;
    private final ServletHandler servletHandler;

    public JettyServer(int port) {
        server = new Server(port);
        servletHandler = new ServletHandler();
        server.setHandler(servletHandler);
    }

    public String getHost() {
        return server.getURI().getHost();
    }

    public int getPort() {
        return server.getURI().getPort();
    }

    public boolean isRunning() {
        return server.isRunning();
    }

    public String getStatus() {
        return isRunning() ? RUNNING : OFF;
    }

    public void addServlet(HttpServlet servlet, String mapping) {
        ServletHolder servletHolder = new ServletHolder();
        servletHolder.setServlet(servlet);
        servletHandler.addServletWithMapping(servletHolder, mapping);
    }

    public void start() {
        startServer();
        join();
    }

    public void stop() {
        stopServer();
        join();
    }

    private void startServer() {
        LOGGER.info("Starting jetty server.");
        try {
            server.start();
        } catch (Exception e) {
            LOGGER.error("Could not start jetty server.", e);
            throw new RuntimeException(e);
        }
    }

    private void stopServer() {
        LOGGER.info("Stopping jetty server.");
        try {
            server.stop();
        } catch (Exception e) {
            LOGGER.error("Could not stop jetty server.", e);
            throw new RuntimeException(e);
        }
    }

    private void join() {
        LOGGER.info("Executing join on jetty server.");
        try {
            server.join();
        } catch (InterruptedException e) {
            LOGGER.error("Could not execute join on jetty server.", e);
            throw new RuntimeException(e);
        }
    }

}
