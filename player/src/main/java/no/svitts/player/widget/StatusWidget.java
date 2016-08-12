package no.svitts.player.widget;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class StatusWidget extends GridPane {

    private Text statusText;

    public StatusWidget(String status, String host, int port) {
        super();
        statusText = new Text(status);
        setPadding(new Insets(15, 0, 15, 0));
        addRow(1, new Label("Status: "), statusText);
        addRow(2, new Label("Address: "), new Text(host + ":" + port));
    }

    public void setStatus(String status) {
        statusText.setText(status);
    }
}
