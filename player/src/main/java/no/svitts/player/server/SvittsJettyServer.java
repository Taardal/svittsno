package no.svitts.player.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;

public class SvittsJettyServer implements JettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SvittsJettyServer.class);
    private static final int PORT = 8181;

    private Server server;
    private ServletHandler servletHandler;

    public SvittsJettyServer() {
        server = new Server(PORT);
        servletHandler = getServletHandler();
        server.setHandler(servletHandler);
    }

    @Override
    public void start() {
        startServer();
    }

    @Override
    public void stop() {
        stopServer();
        join();
    }

    @Override
    public boolean isRunning() {
        return server.isRunning();
    }

    @Override
    public String getUrl() {
        return getHost() + ":" + getPort();
    }

    @Override
    public void addServlet(HttpServlet httpServlet, String mapping) {
        servletHandler.addServletWithMapping(new ServletHolder(httpServlet), mapping);
    }

    private ServletHandler getServletHandler() {
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addFilter(getCrossOriginFilter(), getCrossOriginFilterMapping());
        return servletHandler;
    }

    private FilterHolder getCrossOriginFilter() {
        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,OPTIONS");
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        filterHolder.setName("cross-origin");
        return filterHolder;
    }

    private FilterMapping getCrossOriginFilterMapping() {
        FilterMapping filterMapping = new FilterMapping();
        filterMapping.setFilterName("cross-origin");
        filterMapping.setPathSpec("*");
        return filterMapping;
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

    private String getHost() {
        return server.getURI().getHost();
    }

    private int getPort() {
        return ((ServerConnector) server.getConnectors()[0]).getPort();
    }

}
