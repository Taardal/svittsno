package no.svitts.player.servlet;

import no.svitts.player.userinterface.UserInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlayerServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerServlet.class);

    private final UserInterface userInterface;

    public PlayerServlet(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String videoFilePath = request.getParameter("path");
        String mediaPlayerExecutablePath = userInterface.getExecutablePath();
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
