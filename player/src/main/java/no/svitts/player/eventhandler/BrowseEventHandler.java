package no.svitts.player.eventhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import no.svitts.player.listener.EventListener;

import java.io.File;

public class BrowseEventHandler implements EventHandler<ActionEvent> {

    private Stage stage;
    private EventListener eventListener;
    private FileChooser fileChooser;

    public BrowseEventHandler(Stage stage, EventListener eventListener) {
        this.stage = stage;
        this.eventListener = eventListener;
        fileChooser = getFileChooser();
    }

    public void handle(ActionEvent event) {
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            eventListener.onSetPath(selectedFile.getPath());
        }
    }

    private FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        return fileChooser;
    }

}
