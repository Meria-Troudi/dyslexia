package event.Controllers;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import event.Events_Attributs.Events;
import event.Services.Events_Services;
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
import javafx.stage.Stage;

import javax.swing.*;

public class AfficherEventsController {

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
    private AfficherEventsController afficherEventsController;


    @FXML
    private Button btn_refresh;

    @FXML
    private TextField search_admin;

    @FXML
    void ajouterEvents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event/AjouterEvents.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(getClass().getResource("/event/css/style.css").toExternalForm());
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Ajouter les Événements");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @FXML
    void initialize() throws Exception{
        Events_Services Events_Services = new Events_Services();

        List<Events> events = Events_Services.Display();
        ObservableList<Events> observableList = FXCollections.observableList(events);
        tableview.setItems(observableList);
            E_Title_col.setCellValueFactory(new PropertyValueFactory<>("title"));
            E_Description_col.setCellValueFactory(new PropertyValueFactory<>("description"));
            E_date_col.setCellValueFactory(new PropertyValueFactory<>("date_events"));
            E_Time_col.setCellValueFactory(new PropertyValueFactory<>("time"));
            E_Location_col.setCellValueFactory(new PropertyValueFactory<>("location"));
            E_Type_col.setCellValueFactory(new PropertyValueFactory<>("type"));



        tableview.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tableview.getSelectionModel().getSelectedItem() != null) {
                Events selectedEvent = tableview.getSelectionModel().getSelectedItem();
                openEventDetails(selectedEvent);
            }
        });


        FilteredList<Events> filteredData = new FilteredList<>(observableList, e -> true);

        search_admin.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(e -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return e.getTitle().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Events> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());
        tableview.setItems(sortedData);

    }

    public void openEventDetails(Events selectedEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event/DetailsEvents.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la nouvelle interface
            DetailsEventsController controller = loader.getController();
            controller.setData(selectedEvent, tableview);  // Passer l'événement et le TableView

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'événement");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEvents.fxml"));
            Parent root = loader.load();

            // Récupère le contrôleur et passe l'événement sélectionné ainsi que le TableView
            DetailsEventsController controller = loader.getController();
            controller.setData(selectedEvent, tableview);  // Passe l'événement et la référence de TableView

            // Afficher la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'événement");
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
        this.afficherEventsController = controller;
    }

    @FXML
    void refreshEvents(ActionEvent event) {

        Events_Services Events_Services = new Events_Services();
        try {
            List<Events> events = Events_Services.Display();
            ObservableList<Events> observableList = FXCollections.observableList(events);
            tableview.setItems(observableList);
            E_Title_col.setCellValueFactory(new PropertyValueFactory<>("title"));
            E_Description_col.setCellValueFactory(new PropertyValueFactory<>("description"));
            E_date_col.setCellValueFactory(new PropertyValueFactory<>("date_events"));
            E_Time_col.setCellValueFactory(new PropertyValueFactory<>("time"));
            E_Location_col.setCellValueFactory(new PropertyValueFactory<>("location"));
            E_Type_col.setCellValueFactory(new PropertyValueFactory<>("type"));


        } catch (Exception e){
            System.err.println((e.getMessage()));

        }



    }



}







