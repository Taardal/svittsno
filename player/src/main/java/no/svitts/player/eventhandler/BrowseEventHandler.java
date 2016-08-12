package no.svitts.player.eventhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import no.svitts.player.userinterface.UserInterface;

import java.io.File;

public class BrowseEventHandler implements EventHandler<ActionEvent> {

    private final Stage stage;
    private final UserInterface userInterface;
    private final FileChooser fileChooser;

    public BrowseEventHandler(Stage stage, UserInterface userInterface) {
        this.stage = stage;
        this.userInterface = userInterface;
        fileChooser = getFileChooser();
    }

    public void handle(ActionEvent event) {
        File selectedFile = fileChooser.showOpenDialog(stage);
        userInterface.setPathText(selectedFile.getPath());
    }

    private FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media Player executable");
        return fileChooser;
    }

}
