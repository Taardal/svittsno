package no.svitts.player.eventhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import no.svitts.player.server.JettyServer;

import java.util.logging.Level;
import java.util.logging.Logger;


public class JettyStartStopEventHandler implements EventHandler<ActionEvent> {

    private static final Logger LOGGER = Logger.getLogger(JettyStartStopEventHandler.class.getName());
    private final JettyServer jettyServer;
    private final Button startStopButton;
    private final TextArea eventLog;
    private final Text statusText;

    public JettyStartStopEventHandler(JettyServer jettyServer, Button startStopButton, TextArea eventLog, Text statusText) {
        this.jettyServer = jettyServer;
        this.startStopButton = startStopButton;
        this.eventLog = eventLog;
        this.statusText = statusText;
    }

    @Override
    public void handle(ActionEvent event) {
        if (!jettyServer.isRunning()) {
            startJettyServer();
        } else {
            stopJettyServer();
        }
        statusText.setText(jettyServer.getStatus());
    }

    private void startJettyServer() {
        eventLog.setText("Starting service...");
        try {
            jettyServer.start();
            eventLog.setText(eventLog.getText() + "\n" + "Service started");
        } catch (Exception e) {
            eventLog.setText(eventLog.getText() + "\n" + "Could not start service");
            LOGGER.log(Level.SEVERE, "Could not start JettyServer", e);
        }
        startStopButton.setText("Stop");
    }

    private void stopJettyServer() {
        eventLog.setText("Stopping...");
        try {
            jettyServer.stop();
            eventLog.setText(eventLog.getText() + "\n" + "Service stopped");
        } catch (Exception e) {
            eventLog.setText(eventLog.getText() + "\n" + "Could not stop service");
            LOGGER.log(Level.SEVERE, "Could not stop JettyServer", e);
        }
        startStopButton.setText("Start");
    }

}
