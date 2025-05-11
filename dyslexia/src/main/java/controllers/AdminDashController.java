package controllers;
import entities.Psy;
import entities.Roles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import services.*;
import entities.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
public class AdminDashController implements Initializable {
    @FXML private Label lblStudentCount;
    @FXML private Label lblTeacherCount;
    @FXML private Label lblPsyCount;
    @FXML private VBox pnItems;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cbRoleFilter;
    @FXML private DatePicker dpFromDate, dpToDate;
    private final UserServiceImp userService = new UserServiceImp();
    @FXML private SideBarController sidebarController;
    @FXML private AnchorPane contentArea;
    @FXML private PieChart roleChart;

    private ObservableList<User> masterData;
    private FilteredList<User> filteredData;
    // Method to update the statistics of student, teacher, and psy counts
    private void updateStatistics() {
        List<User> allUsers = userService.getAllUsers();
        long studentCount = allUsers.stream().filter(u -> u instanceof DyslexicStudent).count();
        long teacherCount = allUsers.stream().filter(u -> u instanceof Teacher).count();
        long psyCount = allUsers.stream().filter(u -> u instanceof Psy).count();

        // Updating the labels with the counts
        lblStudentCount.setText(String.valueOf(studentCount));
        lblTeacherCount.setText(String.valueOf(teacherCount));
        lblPsyCount.setText(String.valueOf(psyCount));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<User> all = userService.getAllUsers();
        // --- 0. Compute statistics ---
        updateStatistics();

        // --- 1. Load all users for the table ---
        masterData = FXCollections.observableArrayList(all);
        filteredData = new FilteredList<>(masterData, u -> true);

        // … the rest of your initialize: filters, comboboxes, refreshTable() …

        // 1. Load all users
        masterData = FXCollections.observableArrayList(userService.getAllUsers());
        filteredData = new FilteredList<>(masterData, u -> true);
        // 2. Hook up search text
        txtSearch.textProperty().addListener((obs, old, val) -> applyFilters());
        // 3. Populate role filter
        cbRoleFilter.getItems().addAll("All",
                Roles.ROLE_ADMIN.name(),
                Roles.ROLE_TEACHER.name(),
                Roles.ROLE_STUDENT.name(),
                Roles.ROLE_PSY.name());
        cbRoleFilter.getSelectionModel().select("All");
        cbRoleFilter.valueProperty().addListener((o, a, b) -> applyFilters());
        // 5. Date filters
        dpFromDate.valueProperty().addListener((o, a, b) -> applyFilters());
        dpToDate.valueProperty().addListener((o, a, b) -> applyFilters());
        // 6. Render initial list
        refreshTable();
        populateRoleChart();

    }

    private void applyFilters() {
        String search = txtSearch.getText().toLowerCase().trim();
        String role = cbRoleFilter.getValue();
        LocalDate from = dpFromDate.getValue();
        LocalDate to = dpToDate.getValue();
        filteredData.setPredicate(user -> {
            // Text match
            boolean matchesText = user.getFirstName().toLowerCase().contains(search)
                    || user.getLastName().toLowerCase().contains(search)
                    || user.getEmail().toLowerCase().contains(search);
            // Role filter
            boolean matchesRole = "All".equals(role)
                    || user.getRole().name().equalsIgnoreCase(role);
            // date filter:
            LocalDate created = ((Timestamp)user.getDateCreation())
                    .toLocalDateTime().toLocalDate();
            boolean matchesFrom = from == null || !created.isBefore(from);
            boolean matchesTo   = to   == null || !created.isAfter(to);
            return matchesText(user, search)
                    && matchesRole(user, role)
                    && matchesFrom && matchesTo;
        });

        refreshTable();
    }
    private void refreshTable() {
        pnItems.getChildren().clear();
        for (User u : filteredData) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/Item.fxml"));
                HBox row = loader.load();

                // Wire the controller just like in loadUsers()
                ItemController ic = loader.getController();
                ic.setUser(u, this);

                pnItems.getChildren().add(row);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private HBox createUserRow(User u) {
        // Build an HBox with Labels for each field,
        // or load from an FXML snippet.
        Label lblName = new Label(u.getFirstName() + " " + u.getLastName());
        Label lblEmail = new Label(u.getEmail());
        Label lblRole = new Label(u.getRole().name());
        // … add other labels …
        HBox row = new HBox(20, lblName, lblEmail, lblRole /*,…*/);
        return row;
    }
    // helper methods for clarity:
    private boolean matchesText(User u, String search) {
        return u.getFirstName().toLowerCase().contains(search)
                || u.getLastName().toLowerCase().contains(search)
                || u.getEmail().toLowerCase().contains(search);
    }
    private boolean matchesRole(User u, String role) {
        return "All".equals(role)
                || u.getRole().name().equalsIgnoreCase(role);

    }

    /** Called by ItemController after a delete or edit to refresh the list. */
    public void refresh() {
        updateStatistics();
        applyFilters();

    }

    private void populateRoleChart() {
        List<User> allUsers = userService.getAllUsers();

        long students = allUsers.stream().filter(u -> u instanceof DyslexicStudent).count();
        long teachers = allUsers.stream().filter(u -> u instanceof Teacher).count();
        long psy = allUsers.stream().filter(u -> u instanceof Psy).count();

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                new PieChart.Data("Students", students),
                new PieChart.Data("Teachers", teachers),
                new PieChart.Data("Psychologists", psy)
        );

        roleChart.setData(chartData);
        roleChart.setLegendVisible(true);
        roleChart.setLabelsVisible(true);
        roleChart.setTitle("Users by Role");
    }

}
