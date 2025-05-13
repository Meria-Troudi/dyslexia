package event.Controllers;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import event.Events_Attributs.Events;
import event.Services.Events_Services;
import event.Weather.Api_Weather;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;
import org.json.JSONArray;

import javafx.fxml.FXML;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import javax.swing.*;


public class UserInterfaceController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Events, String> E_Description_col;

    @FXML
    private TableColumn<Events, String> E_Location_col;

    @FXML
    private TableColumn<Events, String> E_Time_col;

    @FXML
    private TableColumn<Events, String> E_Title_col;

    @FXML
    private TableColumn<Events, String> E_Type_col;

    @FXML
    private TableColumn<Events, String> E_date_col;

    @FXML
    private TableView<Events> tableview;
    private AfficherEventsController USERInterfaceController;


    @FXML
    private TextField search_user;


    @FXML
    private Button MyButton;
    @FXML
    private Button MyButton1;

    @FXML
    private ImageView MyImageView;
    @FXML
    private MediaView mediaView;

    @FXML
    private HBox Cardconta;

    @FXML
    void initialize() throws Exception {
//
//        EventService eventService = new EventService();
//        try {
//            List<Event> list= new ArrayList<>();
//            list=eventService.Display();
//
//            for (int i = 0; i < list.size(); i++) {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Card.fxml"));
//                VBox hbox =  fxmlLoader.load();
//                CardCont cardCont = fxmlLoader.getController();
//                cardCont.setdata(list.get(i));
//                Cardconta.getChildren().add(hbox);
//
//
//
//            }




        Events_Services Events_Services = new Events_Services();

        List<Events> events = Events_Services.Display();

        for (int i = 0; i < events.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/event/card.fxml"));
            VBox hbox = fxmlLoader.load();
            CardController cardCont = fxmlLoader.getController();
            cardCont.setData(events.get(i));
            Cardconta.getChildren().add(hbox);
        }

        playVideo();
    }

    private void openEventDetails(Events selectedEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsUSER.fxml"));
            Parent root = loader.load();

            DetailsUSERController controller = loader.getController();
            controller.setData(selectedEvent, tableview);  // on passe l'objet Events entier

            Stage stage = new Stage();
            stage.setTitle("Détails de l'événement");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showEventDetails() throws IOException {
        // Récupère l'événement sélectionné dans le TableView
        Events selectedEvent = tableview.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Charger l'interface FXML pour les détails de l'événement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsUSER.fxml"));
            Parent root = loader.load();

            // Récupère le contrôleur et passe l'événement sélectionné ainsi que le TableView
            DetailsEventsController controller = loader.getController();
            controller.setData(selectedEvent, tableview);  // Passe l'événement et la référence de TableView

            // Afficher la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'événement vous pouvez participez");
            stage.show();
        } else {
            // Si aucun événement n'est sélectionné, affiche un message d'alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun événement sélectionné");
            alert.setContentText("Veuillez sélectionner un événement dans la liste.");
            alert.showAndWait();
        }
    }

    public void refreshTable() {
        Events_Services Events_Services = new Events_Services();
        try {
            List<Events> events = Events_Services.Display();
            ObservableList<Events> observableList = FXCollections.observableList(events);
            tableview.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAfficherEventsController(AfficherEventsController controller) {
        this.USERInterfaceController = controller;
    }

    private final String[] imagePaths = {
            "/images/img1.jpg",
            "/images/img2.jpg",
            "/images/img3.jpg",
            "/images/img4.jpg",
            "/images/img5.jpg"
    };

    private int currentImageIndex = 1;

    @FXML
    void move_img(ActionEvent event) {
        // Avance à l'image suivante
        currentImageIndex = (currentImageIndex + 1) % imagePaths.length;
        showCurrentImage();
    }

    @FXML
    void recall_img(ActionEvent event) {
        // Recule à l'image précédente
        currentImageIndex = (currentImageIndex - 1 + imagePaths.length) % imagePaths.length;
        showCurrentImage();
    }

    private void showCurrentImage() {
        URL imageUrl = getClass().getResource("/event/" + imagePaths[currentImageIndex]);
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm());
            MyImageView.setImage(image);
        } else {
            System.out.println("Image introuvable : " + "/event/"+imagePaths[currentImageIndex]);
        }
    }


    @FXML
    private TextField villeField;

    @FXML
    private Label weatherLabel;

    @FXML
    public void handleWeather() {
        String ville = villeField.getText().trim();
        if (ville.isEmpty()) {
            weatherLabel.setText("Veuillez entrer une ville.");
            return;
        }

        try {
            String json = Api_Weather.getWeather(ville);
            JSONObject obj = new JSONObject(json);

            String description = obj.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = obj.getJSONObject("main").getDouble("temp");

            weatherLabel.setText("Température : " + temp + "°C\n" + "Ciel : " + description);
        } catch (Exception e) {
            weatherLabel.setText("Erreur : ville non trouvée ou connexion échouée.");
            e.printStackTrace();
        }
    }



    @FXML
    private WebView webView; // Tu peux garder ce champ pour d'autres usages si nécessaire, mais il ne sera plus utilisé ici

    // Méthode pour ouvrir la première page dans le navigateur
    public void loadPage1() {
        try {
            URI uri = new URI("https://www.facebook.com/groups/bowde?locale=fr_FR");  // Remplace avec l'URL de ta page
            Desktop.getDesktop().browse(uri);  // Ouvre la page dans le navigateur par défaut
        } catch (Exception e) {
            e.printStackTrace();  // Affiche une erreur si l'URL ne peut pas être ouverte
        }
    }

    // Méthode pour ouvrir la deuxième page dans le navigateur
    public void loadPage2() {
        try {
            URI uri = new URI("https://www.instagram.com/");  // Remplace avec l'URL de ta page
            Desktop.getDesktop().browse(uri);  // Ouvre la page dans le navigateur par défaut
        } catch (Exception e) {
            e.printStackTrace();  // Affiche une erreur si l'URL ne peut pas être ouverte
        }
    }

    private MediaPlayer mediaPlayer;

    @FXML
    private void playVideo() {
        // Chemin du fichier vidéo
        String videoPath = "C:\\projet_java\\untitled\\src\\main\\resources\\videos"; // Remplacez par votre propre chemin

        // Créer un objet Media à partir du fichier
        Media media = new Media(getClass().getResource("/event/videos/vid1.mp4").toExternalForm());

        // Créer un MediaPlayer et l'associer à la MediaView
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        // Démarrer la vidéo
        mediaPlayer.play();
    }


    @FXML
    private VBox mediaContainer;















}









