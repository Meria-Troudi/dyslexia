package event.Controllers;
import event.Mail.EmailSender;
import jakarta.mail.MessagingException;

import event.Participation.Participation_Attributs;
import event.Services.Participation_Services;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FormulaireController {

    @FXML
    private TextArea commentaireField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField telephoneField;

    private int eventId; // Ce champ sera défini avant d'afficher ce formulaire

    // Permet de recevoir l'id de l'événement
    public void setEventId(int id) {
        this.eventId = id;

    }

    @FXML
    void handleParticiper(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        String commentaire = commentaireField.getText();

        // Vérification simple
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        Participation_Attributs participation = new Participation_Attributs(
                eventId, nom, prenom, email, telephone, commentaire
        );
        Participation_Services service = new Participation_Services();
        System.out.println("Selected Event Id : " + eventId);
        if (service.isEventExists(eventId)) {
            try {
                service.Create(participation);
                showAlert("Succès", "Participation enregistrée avec succès !");
                clearFields();
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de l'enregistrement : " + e.getMessage());
            }
        } else {
            showAlert("Erreur", "L'événement n'existe pas dans la base de données.");



        }

        Scene scene1=prenomField.getScene();
        scene1.getWindow().hide();

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
        commentaireField.clear();
    }






}
