package no.svitts.player.eventhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import no.svitts.player.server.JettyServer;
import no.svitts.player.userinterface.UserInterface;

public class JettyStartStopEventHandler implements EventHandler<ActionEvent> {

    private JettyServer jettyServer;
    private UserInterface userInterface;

    public JettyStartStopEventHandler(JettyServer jettyServer, UserInterface userInterface) {
        this.jettyServer = jettyServer;
        this.userInterface = userInterface;
    }

    public void handle(ActionEvent event) {
        if (jettyServer.isRunning()) {
            stopJettyServer();
        } else {
            startJettyServer();
        }
        userInterface.setStatus(jettyServer.getStatus());
    }

    private void startJettyServer() {
        userInterface.addEvent("Starting...");
        jettyServer.start();
        if (jettyServer.isRunning()) {
            userInterface.addEvent("Started successfully.");
            userInterface.setStartStopButtonText("Stop");
        } else {
            userInterface.addEvent("Could not start.");
        }
    }

    private void stopJettyServer() {
        userInterface.addEvent("Stopping...");
        jettyServer.stop();
        if (!jettyServer.isRunning()) {
            userInterface.addEvent("Stopped successfully.");
            userInterface.setStartStopButtonText("Start");
        } else {
            userInterface.addEvent("Could not stop service");
        }
    }

}
