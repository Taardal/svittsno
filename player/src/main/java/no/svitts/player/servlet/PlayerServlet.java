package no.svitts.player.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.player.json.PathDeserializer;
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
    private Gson gson;

    public PlayerServlet(EventListener eventListener) {
        this.eventListener = eventListener;
        gson = new GsonBuilder().registerTypeAdapter(String.class, new PathDeserializer()).create();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String path = getPath(request);
        eventListener.onAddEvent("Received request to play video file [" + path +"]");
        executeCommand("\"" + eventListener.onGetMediaPlayerPath() + "\" \"" + path + "\"");
    }

    private String getPath(HttpServletRequest request) {
        try {
            return gson.fromJson(request.getReader(), String.class);
        } catch (IOException e) {
            String errorMessage = "Could not get path from request.";
            eventListener.onAddEvent(errorMessage);
            LOGGER.error(errorMessage);
            throw new RuntimeException(e);
        }
    }

    private void executeCommand(String command) {
        LOGGER.info("Executing command [{}]", command);
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            eventListener.onAddEvent("Could not execute command [" + command + "]. Error [" + e.getMessage() + "]");
            LOGGER.error("Could not execute command [{}]", command);
            throw new RuntimeException(e);
        }
    }

}
