package no.svitts.player.widget;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import no.svitts.player.eventhandler.BrowseEventHandler;

public class BrowseWidget extends HBox {

    private Text pathText;

    public BrowseWidget(BrowseEventHandler browseEventHandler) {
        super();
        pathText = new Text();
        Button browseButton = new Button("Browse");
        browseButton.setOnAction(browseEventHandler);
        getChildren().addAll(pathText, browseButton);
    }

    public void setPath(String path) {
        pathText.setText(path);
    }

    public String getPath() {
        return pathText.getText();
    }
}
