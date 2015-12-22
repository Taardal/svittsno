package no.svitts.player.userinterface;

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
import no.svitts.player.server.JettyServer;

public class UserInterface extends BorderPane {

    public static final String TITLE = "SvittsPlayerService";
    private final JettyServer jettyServer;

    public UserInterface(JettyServer jettyServer) {
        this.jettyServer = jettyServer;
        setTop(getText(TITLE, "Tahoma", 20));
        setCenter(getLayout());
        setPadding(new Insets(25, 25, 25, 25));
    }

    private Text getText(String string, String font, int fontSize) {
        Text text = new Text(string);
        text.setFont(Font.font(font, FontWeight.NORMAL, fontSize));
        return text;
    }

    private VBox getLayout() {
        VBox layout = new VBox();
        Text statusText = new Text(jettyServer.getStatus());
        layout.getChildren().addAll(getStatusLayout(statusText), getEventLayout(statusText));
        return layout;
    }

    private GridPane getStatusLayout(Text statusText) {
        GridPane statusLayout = new GridPane();
        statusLayout.setPadding(new Insets(15, 0, 15, 0));
        statusLayout.addRow(1, new Label("Status: "), statusText);
        statusLayout.addRow(2, new Label("Address: "), new Text(jettyServer.getAddress() + ":" + jettyServer.getPort()));
        return statusLayout;
    }

    private HBox getEventLayout(Text statusText) {
        HBox eventLayout = new HBox(10);
        TextArea eventLog = getEventLog();
        Button startStopButton = getStartStopButton(statusText, eventLog);
        eventLayout.getChildren().addAll(eventLog, startStopButton);
        return eventLayout;
    }

    private TextArea getEventLog() {
        TextArea eventLog = new TextArea();
        eventLog.setPrefWidth(200);
        eventLog.setPrefHeight(50);
        eventLog.setEditable(false);
        eventLog.setWrapText(true);
        return eventLog;
    }

    private Button getStartStopButton(Text statusText, TextArea eventLog) {
        Button startStopButton = new Button(getStartStopButtonText());
        startStopButton.setPrefHeight(eventLog.getPrefHeight());
        startStopButton.setPrefWidth(50);
        startStopButton.setOnAction(new JettyStartStopEventHandler(jettyServer, startStopButton, eventLog, statusText));
        return startStopButton;
    }

    private String getStartStopButtonText() {
        return jettyServer.isRunning() ? "Stop" : "Start";
    }

}
