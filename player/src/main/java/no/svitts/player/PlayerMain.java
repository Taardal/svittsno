package no.svitts.player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.svitts.player.server.JettyServer;
import no.svitts.player.userinterface.UserInterface;

public class PlayerMain extends Application {

    private JettyServer jettyServer;

    public PlayerMain() {
        jettyServer = new JettyServer(8585);
        jettyServer.addServlet("no.svitts.player.servlet.PlayerServlet", "/");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        jettyServer.start();
        populatePrimaryStage(primaryStage, new UserInterface(jettyServer)).show();
    }

    @Override
    public void stop() throws Exception {
        jettyServer.stop();
    }

    private Stage populatePrimaryStage(Stage primaryStage, UserInterface userInterface) {
        primaryStage.setScene(new Scene(userInterface, primaryStage.getWidth(), primaryStage.getHeight()));
        primaryStage.setTitle(UserInterface.TITLE);
        return primaryStage;
    }

}
