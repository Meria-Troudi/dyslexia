package event.Controllers;

import event.Events_Attributs.Events;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CardController {

    @FXML
    private VBox box;

    @FXML
    private ImageView cardImage;

    @FXML
    private Label titleLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Button detailButton;

    @FXML
    private Button joinButton;

    private Events event;



    public void setData(Events e) {
        this.event = e;
        // Corrected resource path
        URL resource = getClass().getResource("/event/images/fond2.png");

        if (resource == null) {
            System.out.println("Image resource not found! Path might be incorrect.");
        } else {
            System.out.println("Image found at: " + resource.toExternalForm());  // Log the correct resource path
            Image img = new Image(resource.toExternalForm());
            cardImage.setImage(img);
        }

        titleLabel.setText(e.getTitle());
        dateLabel.setText(e.getDate_events());
    }

    @FXML
    void details(MouseEvent e) {
        VBox popupContent = createDetailBox();
        showPopup(popupContent);
    }

    @FXML
    void join(MouseEvent e) {
        // Handle "I'm in" logic here (e.g., register participation)
        System.out.println("Joined event: " + event.getTitle());
    }

    private VBox createDetailBox() {
        VBox detailBox = new VBox();
        detailBox.setPrefSize(183, 310);
        detailBox.setMaxSize(500, 310);
        detailBox.setSpacing(10);
        detailBox.setStyle(
                "-fx-alignment: center;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 4);"
        );

        // Image
        URL resource = getClass().getResource("/event/images/fond2.png");
        if (resource == null) {
            System.out.println("Image resource not found! Path might be incorrect.");
        } else {
            System.out.println("Image found at: " + resource.toExternalForm());
            Image img = new Image(resource.toExternalForm());
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(169);
            imageView.setFitHeight(106);
            imageView.setPreserveRatio(true);
        }

        // Name label
        Label name = new Label("title: " + event.getTitle());
        name.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        // Time label
        Label time = new Label("time: " + event.getTime());
        time.setStyle("-fx-font-size: 20px;");

        // Location label
        Label location = new Label("location: " + event.getLocation());
        location.setStyle("-fx-font-size: 20px;");

        // Type label
        Label type = new Label("type: " + event.getType());
        type.setStyle("-fx-font-size: 20px;");

        // Date label
        Label date = new Label("Date d'event: " + event.getDate_events());
        date.setStyle("-fx-text-fill: #666; -fx-font-size: 20px;");

        // Availability status
        LocalDate d = LocalDate.parse(event.getDate_events());
        Label status = new Label();
        if (d.isBefore(LocalDate.now())) {
            status.setText("Indisponible");
            status.setStyle("-fx-text-fill: red; -fx-font-size: 20px; -fx-font-weight: bold;");
        } else {
            status.setText("Disponible");
            status.setStyle("-fx-text-fill: green; -fx-font-size: 20px; -fx-font-weight: bold;");
        }

        // "I'm in" button
        Button submit = new Button("I'm in");
        submit.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 50;");
        // submit.setOnMouseClicked(this::join);

        // Add all elements to detailBox
        detailBox.getChildren().addAll(name, time, location, type, date, status, submit);

        return detailBox;
    }


    public void subscribe(MouseEvent mouseEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event/Formulaire.fxml"));
            Parent root = loader.load();

            FormulaireController controller = loader.getController();
            controller.setEventId(event.getId_events());  // <<< C’est ici qu’on passe l’ID

            Stage stage = new Stage();
            stage.setTitle("FORMULAIRE");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//popup
    private void showPopup(VBox content) {
        Popup popup = new Popup();
        popup.getContent().add(content);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.setAutoFix(true);

        // Position near detailButton
        Bounds bounds = detailButton.localToScene(detailButton.getBoundsInLocal());
        double x = bounds.getMinX() + 150;
        double y = bounds.getMinY() - 100;
        popup.show(detailButton, x, y);
    }
}
