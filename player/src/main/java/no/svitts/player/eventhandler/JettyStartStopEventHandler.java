package no.svitts.player.eventhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import no.svitts.player.listener.EventListener;
import no.svitts.player.server.JettyServer;

public class JettyStartStopEventHandler implements EventHandler<ActionEvent> {

    private final JettyServer jettyServer;
    private EventListener eventListener;

    public JettyStartStopEventHandler(JettyServer jettyServer, EventListener eventListener) {
        this.jettyServer = jettyServer;
        this.eventListener = eventListener;
    }

    @Override
    public void handle(ActionEvent event) {
        if (jettyServer.isRunning()) {
            stopJettyServer();
        } else {
            startJettyServer();
        }
    }

    private void stopJettyServer() {
        eventListener.onAddEvent("Stopping...");
        jettyServer.stop();
        if (jettyServer.isRunning()) {
            eventListener.onAddEvent("Could not stop.");
        } else {
            eventListener.onAddEvent("Stopped successfully.");
        }
        eventListener.onStatusChanged(jettyServer.isRunning());
    }

    private void startJettyServer() {
        eventListener.onAddEvent("Starting...");
        jettyServer.start();
        if (jettyServer.isRunning()) {
            eventListener.onAddEvent("Started successfully.");
        } else {
            eventListener.onAddEvent("Could not start.");
        }
        eventListener.onStatusChanged(jettyServer.isRunning());
    }

}
