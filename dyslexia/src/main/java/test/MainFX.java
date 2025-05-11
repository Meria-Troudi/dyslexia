package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.UserServiceImp;
import java.io.IOException;

public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize default admin
        new UserServiceImp().createDefaultAdmin();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
            primaryStage.setTitle("LOGIN");
            primaryStage.setScene(scene);
            primaryStage.show();
    }
}
