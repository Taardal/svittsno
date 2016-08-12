package no.svitts.player.userinterface;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import no.svitts.player.eventhandler.BrowseEventHandler;
import no.svitts.player.eventhandler.JettyStartStopEventHandler;
import no.svitts.player.widget.BrowseWidget;
import no.svitts.player.widget.EventWidget;
import no.svitts.player.widget.StatusWidget;
import no.svitts.player.server.JettyServer;

public class UserInterface extends BorderPane {

    public static final String TITLE = "Svitts Player";

    private StatusWidget statusWidget;
    private BrowseWidget browseWidget;
    private EventWidget eventWidget;

    public UserInterface(final JettyServer jettyServer, final Stage stage) {
        statusWidget = new StatusWidget(jettyServer.getStatus(), jettyServer.getHost(), jettyServer.getPort());
        browseWidget = new BrowseWidget(new BrowseEventHandler(stage, this));
        eventWidget = new EventWidget(new JettyStartStopEventHandler(jettyServer, this));
        buildLayout(statusWidget, browseWidget, eventWidget);
    }

    public void setStatus(String status) {
        statusWidget.setStatus(status);
    }

    public void addEvent(String event) {
        eventWidget.addEvent(event);
    }

    public void setStartStopButtonText(String text) {
        eventWidget.setStartStopButtonText(text);
    }

    public void setPath(String path) {
        browseWidget.setPath(path);
    }

    private void buildLayout(StatusWidget statusWidget, BrowseWidget browseWidget, EventWidget eventWidget) {
        setTop(getText(TITLE, "Tahoma", 20));
        setCenter(getCenterLayout(statusWidget, browseWidget, eventWidget));
        setPadding(new Insets(25, 25, 25, 25));
    }

    private VBox getCenterLayout(Node... nodes) {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(nodes);
        return vBox;
    }

    private Text getText(String string, String font, int fontSize) {
        Text text = new Text(string);
        text.setFont(Font.font(font, FontWeight.NORMAL, fontSize));
        return text;
    }
}
