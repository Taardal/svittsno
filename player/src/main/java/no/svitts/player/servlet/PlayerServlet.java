package no.svitts.player.servlet;

import no.svitts.player.listener.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlayerServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerServlet.class);

    private EventListener eventListener;

    public PlayerServlet(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String videoFilePath = request.getHeader("path");
        String mediaPlayerExecutablePath = eventListener.onGetPath();
        eventListener.onAddEvent("Received request to play video file [" + videoFilePath +"]");
        executeCommand("\"" + mediaPlayerExecutablePath + "\" \"" + videoFilePath + "\"");
    }

    private void executeCommand(String command) {
        LOGGER.info("Executing command [{}]", command);
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            LOGGER.error("Could not execute command [{}]", command);
            throw new RuntimeException(e);
        }
    }

}
