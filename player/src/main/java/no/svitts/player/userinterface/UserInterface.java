package no.svitts.player.userinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import no.svitts.player.eventhandler.BrowseEventHandler;
import no.svitts.player.eventhandler.ClearEventLogEventHandler;
import no.svitts.player.eventhandler.JettyStartStopEventHandler;
import no.svitts.player.eventhandler.SaveEventHandler;
import no.svitts.player.listener.EventListener;
import no.svitts.player.reader.FileReader;
import no.svitts.player.server.JettyServer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class UserInterface extends BorderPane implements EventListener {

    public static final String TITLE = "Svitts Player";

    private static final double EVENT_LOG_WIDTH = 500;
    private static final double EVENT_LOG_HEIGHT = 200;
    private static final double PATH_TEXT_FIELD_WIDTH = EVENT_LOG_WIDTH;
    private static final double PATH_TEXT_FIELD_HEIGHT = 30;
    private static final double BUTTON_WIDTH = 60;
    private static final double BUTTON_HEIGHT = 30;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInterface.class);

    private DateTime dateTime;
    private Text statusText;
    private Text urlText;
    private TextField pathTextField;
    private TextArea eventLogTextArea;
    private Button browseButton;
    private Button saveButton;
    private Button clearButton;
    private Button startStopButton;

    public UserInterface(final JettyServer jettyServer, final Stage primaryStage) {
        dateTime = new DateTime();
        statusText = getStatusText(jettyServer);
        urlText = getUrlText(jettyServer);
        pathTextField = getPathTextField();
        eventLogTextArea = getEventLogTextArea();
        browseButton = getButton("Browse", new BrowseEventHandler(primaryStage, this));
        saveButton = getButton("Save", new SaveEventHandler(this));
        clearButton = getButton("Clear", new ClearEventLogEventHandler(this));
        startStopButton = getStartStopButton(jettyServer);
        buildLayout();
    }

    private Button getStartStopButton(JettyServer jettyServer) {
        String startStopButtonText = getStartStopString(jettyServer.isRunning());
        Button button = getButton(startStopButtonText, new JettyStartStopEventHandler(jettyServer, this));
        button.setPrefHeight(EVENT_LOG_HEIGHT - (2 * BUTTON_HEIGHT));
        return button;
    }


    @Override
    public void onStatusChanged(boolean running) {
        statusText.setText(getStatusString(running));
        startStopButton.setText(getStartStopString(running));
    }

    @Override
    public void onSetPath(String path) {
        pathTextField.setText(path);
    }

    @Override
    public String onGetPath() {
        return pathTextField.getText();
    }

    @Override
    public void onAddEvent(String event) {
        event = getPrettyTime() + ": " + event;
        if (eventLogTextArea.getText().isEmpty()) {
            eventLogTextArea.setText(event);
        } else {
            eventLogTextArea.setText(eventLogTextArea.getText() + "\n" + event);
        }
    }

    @Override
    public void onClearEvents() {
        eventLogTextArea.setText("");
    }

    private Text getStatusText(JettyServer jettyServer) {
        return new Text(getStatusString(jettyServer.isRunning()));
    }

    private String getStatusString(boolean running) {
        return running ? "RUNNING" : "OFF";
    }

    private Text getUrlText(JettyServer jettyServer) {
        return new Text(jettyServer.getHost() + ":" + jettyServer.getPort());
    }

    private String getStartStopString(boolean running) {
        return running ? "Stop" : "Start";
    }

    private TextField getPathTextField() {
        TextField textField = new TextField();
        textField.setPrefWidth(PATH_TEXT_FIELD_WIDTH);
        textField.setPrefHeight(PATH_TEXT_FIELD_HEIGHT);
        textField.setText(getSavedPathFromFile());
        return textField;
    }

    private String getSavedPathFromFile() {
        try {
            File pathFile = new File(SaveEventHandler.PATH_FILE);
            if (pathFile.exists()) {
                return new FileReader().readFile(pathFile);
            } else {
                return "";
            }
        } catch (IOException e) {
            String errorMessage = "Could not read path from file [" + SaveEventHandler.PATH_FILE + "]";
            onAddEvent(errorMessage);
            LOGGER.error(errorMessage);
            throw new RuntimeException(e);
        }
    }

    private TextArea getEventLogTextArea() {
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(EVENT_LOG_WIDTH);
        textArea.setPrefHeight(EVENT_LOG_HEIGHT);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        return textArea;
    }

    private Button getButton(String buttonText, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(buttonText);
        button.setOnAction(eventHandler);
        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);
        return button;
    }

    private void buildLayout() {
        setTop(getText(TITLE, "Tahoma", 20));
        setCenter(getCenterLayout());
        setPadding(new Insets(15, 15, 15, 15));
    }

    private Text getText(String string, String font, int fontSize) {
        Text text = new Text(string);
        text.setFont(Font.font(font, FontWeight.NORMAL, fontSize));
        return text;
    }

    private VBox getCenterLayout() {
        GridPane statusLayout = getStatusLayout(statusText, urlText);
        HBox browseLayout = getHBox(pathTextField, browseButton);
        HBox eventLayout = getHBox(eventLogTextArea, getVBox(saveButton, clearButton, startStopButton));
        return getVBox(statusLayout, browseLayout, eventLayout);
    }

    private GridPane getStatusLayout(Text statusText, Text urlText) {
        GridPane gridPane = new GridPane();
        int i = 1;
        gridPane.addRow(i++, new Label("Status: "), statusText);
        gridPane.addRow(i, new Label("URL: "), urlText);
        gridPane.setPadding(new Insets(10, 0, 10, 0));
        return gridPane;
    }

    private VBox getVBox(Node... nodes) {
        VBox vBox = new VBox(5);
        vBox.getChildren().addAll(nodes);
        return vBox;
    }

    private HBox getHBox(Node... nodes) {
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(nodes);
        return hBox;
    }

    private String getPrettyTime() {
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(dateTime.toInstant().getMillis());
    }

}
