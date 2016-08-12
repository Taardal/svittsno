package no.svitts.player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.svitts.player.server.JettyServer;
import no.svitts.player.servlet.PlayerServlet;
import no.svitts.player.userinterface.UserInterface;

public class Main extends Application {

    private final JettyServer jettyServer;

    public Main() {
        jettyServer = new JettyServer(8585);
        jettyServer.addServlet(PlayerServlet.class, "/");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        jettyServer.start();
        prepareStage(primaryStage, new UserInterface(jettyServer)).show();
    }

    @Override
    public void stop() {
        jettyServer.stop();
    }

    private Stage prepareStage(Stage stage, UserInterface userInterface) {
        stage.setScene(new Scene(userInterface, stage.getWidth(), stage.getHeight()));
        stage.setTitle(UserInterface.TITLE);
        return stage;
    }

}
