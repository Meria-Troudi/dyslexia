package event.Controllers;
import java.io.IOException;
import java.time.LocalDate;

import event.Events_Attributs.Events;
import event.Services.Events_Services;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModifierEventsController {


    private Events selectedEvent;


    @FXML
    private DatePicker E_Date;

    @FXML
    private Label E_Date_label;

    @FXML
    private TextArea E_Description;

    @FXML
    private Label E_Description_label;

    @FXML
    private Label E_Id_Events_label;


    @FXML
    private TextField E_Location;

    @FXML
    private Label E_Loctaion_label;

    @FXML
    private TextField E_Time;

    @FXML
    private TextField E_Title;

    @FXML
    private Label E_Title_label;

    @FXML
    private ComboBox<String> E_Type;
    @FXML
    public void initialize() {
        // Initialisation du ComboBox
        E_Type.getItems().addAll(
                "Conférence",
                "Réunion",
                "Atelier",
                "Soirée",
                "Autre"
        );

        // Valeur par défaut optionnelle
        E_Type.setValue("Conférence");
    }
    @FXML
    private Label E_Type_label;

    @FXML
    private Label E_time_label;

    @FXML
    void Update(ActionEvent event) throws Exception {

        // Récupérer les autres valeurs des champs
        String title = E_Title.getText();
        String description = E_Description.getText();
        String date = E_Date.getValue().toString();
        String time = E_Time.getText();
        String location = E_Location.getText();
        String type = E_Type.getValue();
        int idAdmin = 1;

        if(E_Title.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Titre non saisie !");
            alert.setContentText("veuiller saisir les champs !");

// Appliquer la feuille de style à la DialogPane
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("custom-alert"); // optionnel : classe CSS personnalisée

            alert.showAndWait();

        }else {
            if(E_Description.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("Description non saisie !");
                alert.setContentText("veuiller saisir les champs !");

// Appliquer la feuille de style à la DialogPane
                alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                alert.getDialogPane().getStyleClass().add("custom-alert"); // optionnel : classe CSS personnalisée

                alert.showAndWait();

            }else {
                if(!E_Time.getText().matches("^([01]?\\d|2[0-3]):[0-5]\\d$")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText("Time non saisie !");
                    alert.setContentText("Heure invalide ! Format attendu : HH:mm");

// Appliquer la feuille de style à la DialogPane
                    alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                    alert.getDialogPane().getStyleClass().add("custom-alert"); // optionnel : classe CSS personnalisée

                    alert.showAndWait();

                }else {
                    if(E_Location.getText().equals("")){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Info");
                        alert.setHeaderText("Location non saisie !");
                        alert.setContentText("veuiller saisir les champs !");

// Appliquer la feuille de style à la DialogPane
                        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                        alert.getDialogPane().getStyleClass().add("custom-alert"); // optionnel : classe CSS personnalisée

                        alert.showAndWait();

                    }else{
                        int eventId = selectedEvent.getId_events(); // <-- Important !
                        Events events = new Events(eventId, title, description, date, time, location, type, idAdmin);
        // Appeler la méthode Update de ton service
        Events_Services sc = new Events_Services();
        sc.Update(events);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/event/AfficherEvents.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        AfficherEventsController  af = new AfficherEventsController();
                        af.refreshTable();


    }
    }}}
        Scene scene1=E_Description.getScene();
        scene1.getWindow().hide();
    }


   private Events currentEvent;



    public void setEventData(Events event) {
        this.selectedEvent = event; // Important pour récupérer l’ID

        E_Title.setText(event.getTitle());
        E_Description.setText(event.getDescription());
        E_Time.setText(event.getTime());
        E_Location.setText(event.getLocation());
        E_Type.setValue(event.getType());
        E_Date.setValue(LocalDate.parse(event.getDate_events()));





    }



}

