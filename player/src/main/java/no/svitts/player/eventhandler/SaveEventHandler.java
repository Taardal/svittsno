package no.svitts.player.eventhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import no.svitts.player.listener.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SaveEventHandler implements EventHandler<ActionEvent> {

    public static final String PATH_FILE = "player_path.txt";

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveEventHandler.class);

    private EventListener eventListener;

    public SaveEventHandler(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void handle(ActionEvent event) {
        Path filePath = Paths.get(PATH_FILE);
        String mediaPlayerPath = eventListener.onGetMediaPlayerPath();
        try {
            Files.write(filePath, mediaPlayerPath.getBytes());
            eventListener.onAddEvent("Saved path [" + mediaPlayerPath + "] to file [" + filePath.toAbsolutePath().toString() + "]");
        } catch (IOException e) {
            String errorMessage = "Could not save path [" + mediaPlayerPath + "] to file [" + filePath.toAbsolutePath().toString() + "]";
            eventListener.onAddEvent(errorMessage);
            LOGGER.error(errorMessage);
            throw new RuntimeException(e);
        }
    }

}
