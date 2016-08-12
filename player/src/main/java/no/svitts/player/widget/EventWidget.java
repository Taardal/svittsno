package no.svitts.player.widget;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class EventWidget extends HBox {

    private TextArea eventLogTextArea;
    private Button startStopButton;

    public EventWidget(EventHandler<ActionEvent> startStopEventHandler) {
        super();
        eventLogTextArea = getEventLogTextArea();
        startStopButton = new Button("Start");
        startStopButton.setOnAction(startStopEventHandler);
        getChildren().addAll(eventLogTextArea, startStopButton);
    }

    public void setStartStopButtonText(String text) {
        startStopButton.setText(text);
    }

    public void addEvent(String event) {
        eventLogTextArea.setText(eventLogTextArea.getText() + "\n" + event);
    }

    public void clearEvents() {
        eventLogTextArea.setText("");
    }

    private TextArea getEventLogTextArea() {
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(200);
        textArea.setPrefHeight(50);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        return textArea;
    }

}
