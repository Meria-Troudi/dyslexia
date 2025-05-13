package event.Controllers;

import event.Events_Attributs.Events;
import event.Services.Events_Services;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javafx.scene.image.ImageView;

public class AjouterEventsController {

    @FXML
    private DatePicker E_Date;

    @FXML
    private Label E_Date_label;

    @FXML
    private TextArea E_Description;

    @FXML
    private Label E_Description_label;

    @FXML
    private TextField E_Location;

    @FXML
    private Label E_Loctaion_label;

    @FXML
    private TextField E_Time;

    @FXML
    private TextField E_Title;
    @FXML
    private ImageView imageView; // Assure-toi d'ajouter l'ImageView dans ton FXML

    @FXML
    private Button btnUploadImage; // Assure-toi d'ajouter un bouton dans ton FXML pour charger l'image


    @FXML
    private Label E_Title_label;

    @FXML
    private ComboBox<String> E_Type;

    @FXML
    private Button btnRetour;


    private File selectedImageFile;
    public String ch;



    @FXML
    public void initialize() {
        btnRetour.setVisible(false);

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
    // Méthode d'alerte factorisée
    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-alert");
        alert.showAndWait();
    }
    @FXML
    void create(ActionEvent event) throws Exception {
        Events_Services sc = new Events_Services();

        if (E_Title.getText().isEmpty()) {
            showAlert("Titre non saisi !", "Veuillez saisir le titre.");
            return;
        }
        if (E_Description.getText().isEmpty()) {
            showAlert("Description non saisie !", "Veuillez saisir la description.");
            return;
        }
        if (!E_Time.getText().matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            showAlert("Heure invalide !", "Format attendu : HH:mm");
            return;
        }
        if (E_Location.getText().isEmpty()) {
            showAlert("Location non saisie !", "Veuillez saisir le lieu.");
            return;
        }

        // Image (facultatif ou obligatoire selon ton choix)
        String imageName =imageView.getImage().getUrl();
                //(selectedImageFile != null) ? selectedImageFile.getName() : "";

        Events events = new Events(
                E_Title.getText(),
                E_Description.getText(),
                E_Date.getValue().toString(),
                E_Time.getText(),
                E_Location.getText(),
                E_Type.getValue(),
                ch
        );


        sc.Create(events);

        // Fermer la fenêtre
        E_Location.getScene().getWindow().hide();
    }



        /*

        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) btnRetour.getScene().getWindow();

            // Fermer la fenêtre d'ajout d'événements
            currentStage.close();

            // Ouvrir la fenêtre d'affichage des événements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvents.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre AfficherEvents
            AfficherEventsController afficherController = loader.getController();

            // Rafraîchir la table des événements
            afficherController.refreshTable();

            // Créer une nouvelle fenêtre et afficher la vue
           Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher les Événements");
            stage.show();

        } catch (IOException e) {
        }*/

        // Fermer la fenêtre de détails



    // Cette méthode gère le clic sur le bouton "Charger Image"
    @FXML
    void handleImage(ActionEvent event) {

        // Create a FileChooser instance
        FileChooser fileChooser = new FileChooser();

// Set file extension filters for image types
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG Files", "*.jpg", "*.jpeg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Files", "*.png");
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);

// Show the file chooser dialog
        Stage stage = (Stage) imageView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                // Load the selected image into the ImageView
                imageView.setImage(new Image(new FileInputStream(selectedFile)));

                 //Now copy the selected file to the local 'images' folder
                File destDir = new File("/images");
                if (!destDir.exists()) {
                    destDir.mkdirs(); // Create the folder if it doesn't exist
                }

                 //Create a destination file with the same name in the 'images' folder
                File destFile = new File(destDir, selectedFile.getName());

                 //Copy the file to the destination folder
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image copied to: " + destFile.getAbsolutePath());
                ch=selectedFile.getAbsolutePath().toString();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }





}









