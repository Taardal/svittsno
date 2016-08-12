package no.svitts.player.eventhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import no.svitts.player.server.JettyServer;
import no.svitts.player.userinterface.UserInterface;

public class JettyStartStopEventHandler implements EventHandler<ActionEvent> {

    private final JettyServer jettyServer;
    private final UserInterface userInterface;

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
    }

    private void stopJettyServer() {
        userInterface.addEvent("Stopping...");
        jettyServer.stop();
        if (jettyServer.isRunning()) {
            userInterface.addEvent("Could not stop.");
        } else {
            userInterface.addEvent("Stopped successfully.");
        }
        userInterface.setStatus(jettyServer.getStatus());
        userInterface.setStartStopButtonText(getStartStopButtonText());
    }

    private void startJettyServer() {
        userInterface.addEvent("Starting...");
        jettyServer.start();
        if (jettyServer.isRunning()) {
            userInterface.addEvent("Started successfully.");
        } else {
            userInterface.addEvent("Could not start.");
        }
        userInterface.setStatus(jettyServer.getStatus());
        userInterface.setStartStopButtonText(getStartStopButtonText());
    }

    private String getStartStopButtonText() {
        return jettyServer.isRunning() ? "Stop" : "Start";
    }

}
