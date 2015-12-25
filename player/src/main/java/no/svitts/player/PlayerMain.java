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
        jettyServer.addServlet("no.svitts.player.api.PlayerServlet", "/");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        jettyServer.start();
        UserInterface userInterface = new UserInterface(jettyServer);
        prepareStage(primaryStage, userInterface).show();
    }

    @Override
    public void stop() throws Exception {
        jettyServer.stop();
    }

    private Stage prepareStage(Stage stage, UserInterface userInterface) {
        stage.setScene(new Scene(userInterface, stage.getWidth(), stage.getHeight()));
        stage.setTitle(UserInterface.TITLE);
        return stage;
    }

}
