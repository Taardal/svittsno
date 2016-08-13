package no.svitts.player.eventhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import no.svitts.player.listener.EventListener;

public class ClearEventLogEventHandler implements EventHandler<ActionEvent> {

    private EventListener eventListener;

    public ClearEventLogEventHandler(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void handle(ActionEvent event) {
        eventListener.onClearEvents();
    }

}
