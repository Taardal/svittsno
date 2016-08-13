package no.svitts.player.listener;

public interface EventListener {

    void onStatusChanged(boolean running);
    void onSetPath(String path);
    String onGetPath();
    void onAddEvent(String event);
    void onClearEvents();

}
