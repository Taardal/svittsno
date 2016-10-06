package no.svitts.player.listener;

public interface EventListener {

    void onStatusChanged(boolean running);
    void onSetMediaPlayerPath(String path);
    String onGetMediaPlayerPath();
    void onAddEvent(String event);
    void onClearEvents();

}
