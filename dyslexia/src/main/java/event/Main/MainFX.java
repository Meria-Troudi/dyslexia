package event.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import javafx.scene.image.Image;


public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface.fxml"));

        Parent root = loader.load();
        Scene sc = new Scene(root);
        //Stage.getScene(sc);
        sc.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        //sc.getStylesheets().add(getClass().getResource("/css/sideBar.css").toExternalForm());

        primaryStage.setTitle("INTERFACE");

      // primaryStage.getIcons().add(new Image("/images/logo.JPG") {});
        primaryStage.setScene(sc);
        primaryStage.show();


    }

}
