package event.Controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SideBarController {
    @FXML private VBox mainArea;
    @FXML private AnchorPane topBar;
    @FXML
    private HBox hboxsidebar;  // The HBox you want to adjust position for
    @FXML
    private VBox sideBar;      // The sideBar VBox that contains the buttons

    private boolean isSidebarExpanded = false;
    @FXML
    private Button btnCourses,btnDashboard,btnEvents,btnPsychologists,btnPublications,btnRec,btnSettings,btn_notif;
    @FXML
    private  Pane contentArea;

    @FXML
    private Button toggleButton;
    @FXML
    private Label dashboardLabel, coursesLabel, publicationsLabel, psyLabel, eventsLabel, recLabel, settingsLabel;
    private boolean isExpanded = false;

    private static final double EXPANDED_WIDTH = 300;
    private static final double COLLAPSED_WIDTH = 70;

    private static final double EXPANDED_HBOX_X = 820;
    private static final double COLLAPSED_HBOX_X = 960;

    private static final Duration SIDEBAR_ANIMATION_DURATION = Duration.millis(300);
    private static final Duration LABEL_FADE_DURATION = Duration.millis(150);
    private List<Label> labels;


    @FXML
    void goToCourses(ActionEvent event) {
      //  loadContent("/Settings.fxml"); // Load the "courses.fxml" into contentArea
    }

    @FXML
    void goToDashboard(ActionEvent event) {
       // loadContent("/Settings.fxml"); // Load the "dashboard.fxml" into contentArea
    }


    @FXML
    void goToEvents(ActionEvent event) {
       // loadContent("/Settings.fxml"); // Load the "events.fxml" into contentArea
    }

    @FXML
    void goToLogout(ActionEvent event) {
        
    }

    @FXML
    void goToPsychologists(ActionEvent event) {
       // loadContent("/Settings.fxml"); // Load the "psychologists.fxml" into contentArea
    }

    @FXML
    void goToPublications(ActionEvent event) {
       // loadContent("/Settings.fxml"); // Load the "publications.fxml" into contentArea
    }

    @FXML
    void goToRec(ActionEvent event) {
    //    loadContent("/Settings.fxml"); // Load the "rec.fxml" into contentArea
    }

    @FXML
    void goToSettings(ActionEvent event) {
        loadContent("/Settings.fxml"); // Load the "settings.fxml" into contentArea
    }

    private void loadContent(String fxmlFile) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sideBar.fxml"));
            Parent content = loader.load();

            // Set the loaded content to the contentArea pane
            contentArea.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 /////////////////////////////////////////////////////////////////////
 @FXML
 public void initialize() {
     labels = List.of(dashboardLabel, coursesLabel, publicationsLabel,
             psyLabel, eventsLabel, recLabel, settingsLabel);

     sideBar.setPrefWidth(COLLAPSED_WIDTH);
     labels.forEach(lbl -> {
         lbl.setVisible(false);
         lbl.setOpacity(0);
     });

     hboxsidebar.setLayoutX(COLLAPSED_HBOX_X);
 }

    @FXML
    private void toggleSidebar() {
        double targetWidth = isExpanded ? COLLAPSED_WIDTH : EXPANDED_WIDTH;
        double targetX = isExpanded ? COLLAPSED_HBOX_X : EXPANDED_HBOX_X;

        // Animate width and hbox position
        Timeline tl = new Timeline(
                new KeyFrame(SIDEBAR_ANIMATION_DURATION,
                        new KeyValue(sideBar.prefWidthProperty(), targetWidth, Interpolator.EASE_BOTH),
                        new KeyValue(hboxsidebar.layoutXProperty(), targetX, Interpolator.EASE_BOTH)
                )
        );
        tl.play();

        // Animate labels
        for (Label lbl : labels) {
            if (!isExpanded) {
                lbl.setVisible(true);
                lbl.setOpacity(0);
            }

            KeyValue start = new KeyValue(lbl.opacityProperty(), isExpanded ? 1.0 : 0.0);
            KeyValue end = new KeyValue(lbl.opacityProperty(), isExpanded ? 0.0 : 1.0);

            Timeline fade = new Timeline(
                    new KeyFrame(Duration.ZERO, start),
                    new KeyFrame(LABEL_FADE_DURATION, end)
            );
            if (isExpanded) fade.setOnFinished(e -> lbl.setVisible(false));
            fade.play();
        }

        isExpanded = !isExpanded;}
}
