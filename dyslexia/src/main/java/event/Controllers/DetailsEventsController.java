package event.Controllers;
import event.Events_Attributs.Events;
import event.Events_Attributs.Events;

import event.Participation.Participation_Attributs;
import event.Services.Events_Services;
import event.Services.Participation_Services;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;
import java.util.Optional;


public class DetailsEventsController {

    @FXML
    private Label dateLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Button btnSupprimer;

    private Events currentEvent;


    //private Events currentEvent;
    private TreeItem<Events> currentTreeItem;


    private TableView<Events> tableview;
    private DetailsEventsController DetailsEventsController;



    /// /

    @FXML
    private TableView<Participation_Attributs> participationTableView;


    @FXML
    private TableColumn<Participation_Attributs, String> nom;

    @FXML
    private TableColumn<Participation_Attributs, String> prenom ;

    @FXML
    private TableColumn<Participation_Attributs, String> email ;

    @FXML
    private TableColumn<Participation_Attributs, String> telephone;

    @FXML
    private TableColumn<Participation_Attributs, String> commentaire;

    @FXML
    private TableColumn<Participation_Attributs, String> date_participation;


    @FXML
    void initialize() {
        Participation_Services Participation_Services = new Participation_Services();
        try {
            List<Participation_Attributs> participation = Participation_Services.Display();
            ObservableList<Participation_Attributs> observableList = FXCollections.observableList(participation);
            participationTableView.setItems(observableList);
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
            commentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
            date_participation.setCellValueFactory(new PropertyValueFactory<>("date_participation"));


        } catch (Exception e){
            System.err.println((e.getMessage()));

        }}





    public void setData(Events event, TableView<Events> tableview) {
        this.currentEvent = event;
        this.tableview = tableview;

        // Affichage des informations de l'événement dans les labels
        titleLabel.setText(event.getTitle());
        descriptionLabel.setText(event.getDescription());
        dateLabel.setText(event.getDate_events().toString());
        timeLabel.setText(event.getTime());
        locationLabel.setText(event.getLocation());
        typeLabel.setText(event.getType());



        /// //////////



        Participation_Services ps = new Participation_Services();
        try {
            List<Participation_Attributs> participations = ps.DisplayByEvent(event.getId_events());
            ObservableList<Participation_Attributs> observableList = FXCollections.observableList(participations);
            participationTableView.setItems(observableList);
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
            commentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
            date_participation.setCellValueFactory(new PropertyValueFactory<>("date_participation"));
        } catch (Exception e) {
            e.printStackTrace();
        }












    }

    // Méthode de suppression
    @FXML
    private void supprimerEvenement(ActionEvent event) {
        if (currentEvent != null) {
            // Confirmation de la suppression
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet événement ?");
            alert.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Events_Services eventService = new Events_Services();
                try {
                    eventService.Delete(currentEvent.getId_events());
                } catch (Exception e){
                    e.printStackTrace();  // Affiche l'exception pour débogage
                    // Tu peux aussi afficher un message d'erreur à l'utilisateur ici si nécessaire
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Erreur");
                    alert1.setHeaderText("Erreur lors de la suppression de l'événement");
                    alert1.setContentText("Une erreur s'est produite. Veuillez réessayer.");
                    alert1.showAndWait();
                }
                // Supprimer l'événement du TableView dans la fenêtre principale

                if (tableview != null && currentEvent != null) {
                    tableview.getItems().removeIf(eventItem -> eventItem.getId_events() == currentEvent.getId_events());

                }



                // Fermer la fenêtre de détails
               Stage stage = (Stage) titleLabel.getScene().getWindow();
                stage.close();
            }
        }
    }


    @FXML
    void modifierEvenement(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event/ModifierEvents.fxml"));
            Parent root = loader.load();
            //Récupérer le contrôleur et passer l'événement actuel
            ModifierEventsController controller = loader.getController();
            controller.setEventData(currentEvent); // <-- LE PLUS IMPORTANT

            Scene scene1=dateLabel.getScene();
            scene1.getWindow().hide();


            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Modifier les Événements");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }









}
