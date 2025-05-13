package event.Controllers;

import event.Events_Attributs.Events;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

import java.net.URL;

public class DetailsUSERController {

    @FXML
    private Button btnparticiper;

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

    /// ////////////
    @FXML
    private void formulaireEvents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event/Formulaire.fxml"));
            Parent root = loader.load();

            FormulaireController controller = loader.getController();
            controller.setEventId(this.eventId);  // <<< C’est ici qu’on passe l’ID

            Stage stage = new Stage();
            stage.setTitle("FORMULAIRE");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }


        Scene scene1=locationLabel.getScene();
        scene1.getWindow().hide();
    }



    private Events currentEvent;

    //private Events currentEvent;
    private TreeItem<Events> currentTreeItem;
    private TableView<Events> tableview;





    /// /////////////////
    private int eventId;  // champ ajouté

    public void setData(Events event, TableView<Events> tableview) {
        this.currentEvent = event;
        this.tableview = tableview;
        this.eventId = event.getId_events();  // stocke l'ID ici !

        // Remplir les labels
        titleLabel.setText(event.getTitle());
        descriptionLabel.setText(event.getDescription());
        dateLabel.setText(event.getDate_events().toString());
        timeLabel.setText(event.getTime());
        locationLabel.setText(event.getLocation());
        typeLabel.setText(event.getType());
    }


}
