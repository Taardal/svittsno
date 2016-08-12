package no.svitts.player.userinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import no.svitts.player.eventhandler.JettyStartStopEventHandler;
import no.svitts.player.server.JettyServer;

public class UserInterface extends BorderPane {

    public static final String TITLE = "Svitts Player";

    private final String host;
    private final int port;
    private final EventHandler<ActionEvent> jettyStartStopEventHandler;
    private final Text statusText;
    private final TextArea eventTextArea;
    private final Button startStopButton;

    public UserInterface(JettyServer jettyServer) {
        host = jettyServer.getHost();
        port = jettyServer.getPort();
        jettyStartStopEventHandler = new JettyStartStopEventHandler(jettyServer, this);
        statusText = new Text();
        eventTextArea = getEventTextArea();
        startStopButton = getStartStopButton(eventTextArea);
        buildLayout();
    }

    public void setStatus(String status) {
        statusText.setText(status);
    }

    public void addEvent(String event) {
        eventTextArea.setText(eventTextArea.getText() + "\n" + event);
    }

    public void setStartStopButtonText(String startStopButtonText) {
        startStopButton.setText(startStopButtonText);
    }

    private TextArea getEventTextArea() {
        TextArea eventLog = new TextArea();
        eventLog.setPrefWidth(200);
        eventLog.setPrefHeight(50);
        eventLog.setEditable(false);
        eventLog.setWrapText(true);
        return eventLog;
    }

    private Button getStartStopButton(TextArea eventLog) {
        Button startStopButton = new Button("Start");
        startStopButton.setPrefHeight(eventLog.getPrefHeight());
        startStopButton.setPrefWidth(50);
        startStopButton.setOnAction(jettyStartStopEventHandler);
        return startStopButton;
    }

    private void buildLayout() {
        setCenter(getCenterLayout());
        setTop(getText(TITLE, "Tahoma", 20));
        setPadding(new Insets(25, 25, 25, 25));
    }

    private VBox getCenterLayout() {
        VBox layout = new VBox();
        layout.getChildren().addAll(getStatusLayout(statusText), getEventLayout());
        return layout;
    }

    private GridPane getStatusLayout(Text statusText) {
        GridPane statusLayout = new GridPane();
        statusLayout.setPadding(new Insets(15, 0, 15, 0));
        statusLayout.addRow(1, new Label("Status: "), statusText);
        statusLayout.addRow(2, new Label("Address: "), new Text(host + ":" + port));
        return statusLayout;
    }

    private HBox getEventLayout() {
        HBox eventLayout = new HBox(10);
        eventLayout.getChildren().addAll(eventTextArea, startStopButton);
        return eventLayout;
    }

    private Text getText(String string, String font, int fontSize) {
        Text text = new Text(string);
        text.setFont(Font.font(font, FontWeight.NORMAL, fontSize));
        return text;
    }
}
