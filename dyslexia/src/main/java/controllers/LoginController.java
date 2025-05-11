package controllers;

import entities.*;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Duration;
import services.OTPService;
import services.UserDao;
import services.UserServiceImp;

import java.util.prefs.Preferences;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.session.UserSession;
import java.io.IOException;
public class LoginController {
    public ImageView logoImage;
    public Text errormail,errorpwd,fterror,merror,perror,cperror,psyErrorText;
    public TextField tfFirstName,tfLastName,tfPone,ftmail;
    public TextField availabilityField,subjectAreaField,specificNeedsField,tfBio,tfschedule;
    public PasswordField ftCPwd,ftPwd;
    public DatePicker tfbirth;
    public ComboBox<String> severityLevelComboBox,dyslexiaTypeComboBox,roleComboBox;
    public CheckBox rememberMeCheckbox;
    @FXML private TextField signInEmail;
    @FXML private PasswordField signInPassword;
    @FXML private Button backToStep1FromTeacher, backToStep1FromStudent, toSignUpButton, toStep2Button,backToStep1Frompsy;
    @FXML private VBox signInPane, signUpStep1Pane, teacherStepPane, studentStepPane,leftPane,PsyStepPane;
    @FXML private RadioButton maleRadio, femaleRadio;
    @FXML private StackPane rightFormPane;
    @FXML private Label descriptionLabel, welcomeLabel,invalidLogin;
    private UserServiceImp serviceUser = new UserServiceImp();
    private final UserDao userDao = new UserDao();
    private boolean isSignUpMode = false;
    private boolean isSwitched = false;
    private User user;
    private Preferences prefs;
    private static final String PREF_EMAIL    = "remember_email";
    private static final String PREF_PASSWORD = "remember_password";
    private static final String PREF_REMEMBER = "remember_me";
    public void setUser(User user) {
        this.user = user;
        System.out.println("Welcome " + user.getFirstName());
    }

    @FXML
    public void initialize() {
        prefs = Preferences.userNodeForPackage(LoginController.class);
        boolean remember = prefs.getBoolean(PREF_REMEMBER, false);
        rememberMeCheckbox.setSelected(remember);
        if (remember) {signInEmail.setText(prefs.get(PREF_EMAIL, ""));
                         signInPassword.setText(prefs.get(PREF_PASSWORD, ""));
        }
        backToStep1FromTeacher.setOnAction(e -> backToStep1());
        backToStep1FromStudent.setOnAction(e -> backToStep1());
        backToStep1Frompsy.setOnAction(e -> backToStep1());
        signInPane.setVisible(true);
        signUpStep1Pane.setVisible(false);
        teacherStepPane.setVisible(false);
        studentStepPane.setVisible(false);
        PsyStepPane.setVisible(false);
        toSignUpButton.setOnAction(e -> {
            if (isSignUpMode) { switchToSignIn();
            } else {switchToSignUpStep1(); }
        });
        ToggleGroup genderGroup = new ToggleGroup();
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);
        toStep2Button.setDisable(true);
        roleComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean roleChosen = newVal != null && !newVal.isEmpty();
            toStep2Button.setDisable(!roleChosen);
            if (roleChosen) configureStep2Button();
        });}
   /////////////register//////////////////////
    void clearError(){
        errormail.setText("");errorpwd.setText("");
        fterror.setText("");merror.setText("");
    }
    private void configureStep2Button() {
            String selectedRole = roleComboBox.getValue();
            toStep2Button.setVisible(true);
            toStep2Button.setOnAction(null);
            if ("Admin".equalsIgnoreCase(selectedRole)) {
                toStep2Button.setText("Sign Up");
                toStep2Button.setOnAction(this::handleSignUp);
            } else {
                toStep2Button.setText("Next →");
                toStep2Button.setOnAction(ev -> {
                    boolean isValid = validateStep1();
                    if (isValid) {goToStep2();
                    } else { showAlert("Please correct errors before proceeding."); }
                });
            }
    }

    private boolean validateStep1() {
        boolean isValid = true;
        int phoneNumber;
        try {
            if (tfPone.getText().isEmpty()) {
                fterror.setText("Phone number is required.");
                tfPone.getStyleClass().add("error");
                isValid = false;
            } else if (tfPone.getText().length()!=8) {
                fterror.setText("Phone number must contain at 8 characters.");
                tfPone.getStyleClass().add("error");
                isValid = false;
            }
        } catch (NumberFormatException e)
        {   tfPone.setText("Invalid phone number");
            tfPone.getStyleClass().add("error");
            isValid=false;
        }
        // name validation
        if (tfFirstName.getText().isEmpty()) {
            tfFirstName.getStyleClass().add("error");
            fterror.setText("First name is required");
            isValid = false;
        } else if (tfFirstName.getText().length()<4) {
            fterror.setText("First name must contain at least 4 characters.");
            tfFirstName.getStyleClass().add("error");
            isValid = false;
        }
        if (tfLastName.getText().isEmpty()) {
            fterror.setText("Last name is required");
            tfLastName.getStyleClass().add("error");
            isValid = false;
        } else if (tfLastName.getText().length()<3) {
            fterror.setText("Last name must contain at least 4 characters.");
            tfLastName.getStyleClass().add("error");
            isValid = false;
        }
        // Email validation
        System.out.println("Email: " + ftmail.getText());
        if (ftmail.getText().isEmpty()) {
            ftmail.getStyleClass().add("error");
            merror.setText("Email address is required.");
            isValid = false;
        }else if (!ftmail.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            merror.setText("invalid email address.");
            ftmail.getStyleClass().add("error");
            isValid = false;
        }else if(serviceUser.emailExists(ftmail.getText())){
            merror.setText("Email already exists.");
            ftmail.getStyleClass().add("error");
            isValid = false;
        }
        // Password validation
        if (ftPwd.getText().isEmpty()) {
            perror.setText("Password is required");
            ftPwd.getStyleClass().add("error");
            isValid = false;
        } else if (ftPwd.getText().length()<8) {
            ftPwd.getStyleClass().add("error");
            perror.setText("Password must contain at least 8 characters.");
            isValid = false;
        }
        // Confirm password validation
        if (!ftCPwd.getText().equals(ftPwd.getText())) {
            ftCPwd.getStyleClass().add("error");
            isValid = false;
            cperror.setText("Passwords do not match");
        }
        // Gender validation
        if (!maleRadio.isSelected() && !femaleRadio.isSelected()) {
            isValid = false;
            fterror.setText("Select your gender");
        }
        // Birthdate validation
        if (tfbirth.getValue() == null) {
            tfbirth.getStyleClass().add("error");
            isValid = false;
            fterror.setText("Select your birth date (MM/DD/YYYY)");
        } else if (tfbirth.getValue().isAfter(LocalDate.now().minusYears(6))) {
            tfbirth.getStyleClass().add("error");
            fterror.setText("You must be at least 6 years old");
            isValid = false;
        }
        // **Role**
        String selectedRole = roleComboBox.getValue();
        if (selectedRole == null) {isValid = false;
            fterror.setText("Please select a role");
            roleComboBox.getStyleClass().add("error");
        }
        if (isValid) {
            createUserObject();
        }return isValid;
    }

    private boolean validatePsyFields() {
        boolean ok = true;
        if (tfschedule.getText().isBlank()) {ok = false;
            showAlert("Psy Information: Please select a Dyslexia Type.");
        }
        if (tfBio.getText().isBlank()) {ok = false;
            showAlert("Psy Information: Please select a Severity Level.");
        }
      return ok;
    }
    private boolean validateTeacherFields() {
        boolean ok = true;
        if (subjectAreaField.getText().isBlank()) {ok = false;
            subjectAreaField.getStyleClass().add("error");
            showAlert("Subject Area is required.");
        }
        if (availabilityField.getText().isBlank()) {
            ok = false;
            availabilityField.getStyleClass().add("error");
            showAlert("Availability is required.");
        }return ok;
    }

    private boolean validateStudentFields() {
        boolean ok = true;
        if (dyslexiaTypeComboBox.getValue() == null) { ok = false;
            showAlert("Student Information Please select a Dyslexia Type.");
        }
        if (severityLevelComboBox.getValue() == null) {ok = false;
            showAlert("Student Information Please select a Severity Level.");
        }
        if (specificNeedsField.getText().isBlank()) {ok = false;
            specificNeedsField.getStyleClass().add("error");
            showAlert("Student Information Specific Needs cannot be empty.");
        }return ok;
    }

    @FXML
    private void goToNextStep() {
        if (toStep2Button.isDisabled()) { // If the first step is valid, go to the second step
            return;
        }signUpStep1Pane.setVisible(false);
         teacherStepPane.setVisible(false); // or studentStepPane, depending on the user role
    }
    private void createUserObject() {
        String selectedRole = roleComboBox.getValue();
        if (selectedRole == null) {
            throw new IllegalStateException("Role must be selected before creating user");
        }        Roles roleEnum = Roles.valueOf("ROLE_" + selectedRole.toUpperCase());
        switch (roleEnum) {
            case ROLE_TEACHER:
                user = new Teacher();
                break;
            case ROLE_STUDENT:
                user = new DyslexicStudent();
                break;
            case ROLE_PSY:
                user = new Psy();
        }
        user.setRole(roleEnum);
        user.setFirstName(tfFirstName.getText());
        user.setLastName(tfLastName.getText());
        user.setPhoneNumber(Integer.parseInt(tfPone.getText()));
        user.setEmail(ftmail.getText());
        user.setBirthDay(java.sql.Date.valueOf(tfbirth.getValue()));
        user.setPassword(ftPwd.getText());
        user.setGender(maleRadio.isSelected() ? "Male" : "Female");
    }
    @FXML
    private void  handleSignUp(ActionEvent event) {
        if (user instanceof Teacher) {
            if (!validateTeacherFields()) return;
        } else if (user instanceof DyslexicStudent) {
            if (!validateStudentFields()) return;
        }else if (user instanceof Psy) {
            if (!validatePsyFields()) return;
        }
        if (user instanceof Teacher) {
            Teacher t = (Teacher) user;
            t.setSubjectArea(subjectAreaField.getText());
            t.setAvailability(availabilityField.getText());
        } else if (user instanceof DyslexicStudent) {
            DyslexicStudent s = (DyslexicStudent) user;
            s.setDyslexiaType(DyslexiaType.valueOf(dyslexiaTypeComboBox.getValue().toUpperCase()));
            s.setSeverityLevel(SeverityLevel.valueOf(severityLevelComboBox.getValue().toUpperCase()));
            s.setSpecificNeeds(specificNeedsField.getText());
        }
        else if (user instanceof Psy) {
            Psy p = (Psy) user;
            p.setSchedule(tfschedule.getText());
            p.setBiography(tfBio.getText());
        }
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Email Verification");
        info.setHeaderText(null);
        info.setContentText("An email verification code will be sent to you. Please check your inbox (and spam folder).");
        info.showAndWait();
        try {
            user.setDateCreation(new Date());

            // Generate 6-digit OTP
            String token = OTPService.generateOTP(user.getEmail(), 6);
            user.setResetToken(token); // Store token in user
            user.setEnabled(false);    // Account disabled until token is verified

            // Save user
            serviceUser.createUser(user);

            // Send token via email
            OTPService.sendOTP(user.getEmail(), token);

            // Show token verification dialog
            showTokenVerificationDialog(user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'inscription.");
        }
    }
    private void showTokenVerificationDialog(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/CodeConf.fxml"));
            Parent root = loader.load();
            CodeConfirmationController controller = loader.getController();
            controller.setUserEmail(email); // Pass email to dialog

            Stage stage = new Stage();
            stage.setTitle("Validation de l'email");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Show as modal dialog
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Impossible de charger la fenêtre de validation.");
        }
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void goToStep2() {
        String selectedRole = roleComboBox.getValue();
        if (selectedRole == null) {
            showAlert("Please select your role before proceeding.");
            return;
        }
        signUpStep1Pane.setVisible(false);
        switch (selectedRole.toLowerCase()) {
            case "teacher":
                teacherStepPane.setVisible(true);
                animateFade(teacherStepPane);
                break;
            case "student":
                studentStepPane.setVisible(true);
                animateFade(studentStepPane);
                break;
            case "psy":
                PsyStepPane.setVisible(true);
                animateFade(PsyStepPane);
                break;
            default:
                // (should never happen if button is disabled when role is null/invalid)
                showAlert("Invalid role selected.");
        }
    }
    private void backToStep1() {
        teacherStepPane.setVisible(false);
        studentStepPane.setVisible(false);
        signUpStep1Pane.setVisible(true);
        animateFade(signUpStep1Pane);
    }
    private void switchToSignUpStep1() {
        animateSwitchSides();
        signInPane.setVisible(false);
        signUpStep1Pane.setVisible(true);
        teacherStepPane.setVisible(false);
        studentStepPane.setVisible(false);
        PsyStepPane.setVisible(false);
        animateFade(signUpStep1Pane);
        toSignUpButton.setText("Sign In");
        welcomeLabel.setText("Join Us!");
        descriptionLabel.setText("Create your account and start learning!");
        isSignUpMode = true;
    }


    //////////// sign in/////////////////////////
    public boolean validateSignInForm() {
        String email = signInEmail.getText();
        String password = signInPassword.getText();
        if (email == null || email.isEmpty() || !isValidEmail(email)) {
            return false;
        }
        if (password == null || password.isEmpty()) {
            return false;
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    @FXML
    public void handleLogin() {
        if (!validateSignInForm()) {
            invalidLogin.setText("Email invalide.");
            return;}
        String email = signInEmail.getText().trim();
        String password = signInPassword.getText().trim();

        try {
            boolean isLogged = userDao.login(email, password);
            if (!isLogged) { invalidLogin.setText("Email ou mot de passe incorrect.");
                return;
            }
            User user = UserSession.CURRENT_USER.getUserLoggedIn();
            // Vérifier si le compte est activé
                if (!user.isEnabled()) {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Account Verification Required");
                    info.setHeaderText(null);
                    info.setContentText("Your account is not yet verified. A new verification code will be sent to your email.");
                    info.showAndWait();
                    // 1. generate a new 6‑digit code
                    String token = OTPService.generateOTP(email, 6);
                    // 2. email it
                    boolean sent = OTPService.sendOTP(email, token);
                    if (!sent) {
                        invalidLogin.setText("Impossible d’envoyer le code. Réessayez plus tard.");
                        return;
                    }
                    showCodeConfirmationDialog(email);
                    return;
                }
            // Étape 4 : Connexion réussie
            redirectToDashboard(user);
            // Handle "Remember Me"
            if (rememberMeCheckbox.isSelected()) {
                prefs.putBoolean(PREF_REMEMBER, true);
                prefs.put(PREF_EMAIL, email);
                prefs.put(PREF_PASSWORD, password);
            } else {
                prefs.putBoolean(PREF_REMEMBER, false);
                prefs.remove(PREF_EMAIL);
                prefs.remove(PREF_PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            invalidLogin.setText("Erreur interne. Veuillez réessayer.");
        }
    }
    private void showCodeConfirmationDialog(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/CodeConf.fxml"));
            Parent root = loader.load();

            CodeConfirmationController controller = loader.getController();
            controller.setUserEmail(email);

            Stage stage = new Stage();
            stage.setTitle("Confirm Your Email");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Cannot load confirmation screen.");
        }
    }

    private void redirectToDashboard(User user) {
        Roles role = user.getRole();
        System.out.println("Retrieved role: " + user.getRole());

        try {
            FXMLLoader loader;
            Parent root;
            Stage stage = (Stage) signInEmail.getScene().getWindow();
            stage.setTitle("Dashboard");

            if (role == Roles.ROLE_ADMIN) {
                loader = new FXMLLoader(getClass().getResource("/user/AdminDash.fxml"));
            } else if (role == Roles.ROLE_TEACHER) {
                loader = new FXMLLoader(getClass().getResource("/user/StDash.fxml"));
            } else if (role == Roles.ROLE_STUDENT) {
                loader = new FXMLLoader(getClass().getResource("/user/StDash.fxml"));
            }  else if (role == Roles.ROLE_PSY) {
                loader = new FXMLLoader(getClass().getResource("/user/PsyDash.fxml"));
            }else {
                showAlert("Rôle non reconnu.");
                return;
            }
            root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur lors du chargement du tableau de bord.");
        }
    }
    private void switchToSignIn() {
        animateSwitchSides();
        signInPane.setVisible(true);
        signUpStep1Pane.setVisible(false);
        teacherStepPane.setVisible(false);
        studentStepPane.setVisible(false);
        PsyStepPane.setVisible(false);
        animateFade(signInPane);
        toSignUpButton.setText("Create Account");
        welcomeLabel.setText("Welcome back!");
        descriptionLabel.setText("Sign in with your personal info");
        isSignUpMode = false;
    }
    private void animateSwitchSides() {
        double moveDistance = 450;
        TranslateTransition ttLeft = new TranslateTransition(Duration.millis(400), leftPane);
        TranslateTransition ttRight = new TranslateTransition(Duration.millis(400), rightFormPane);
        if (!isSwitched) {
            ttLeft.setByX(moveDistance);
            ttRight.setByX(-moveDistance);
        } else {
            ttLeft.setByX(-moveDistance);
            ttRight.setByX(moveDistance);
        }
        ttLeft.play();
        ttRight.play();
        isSwitched = !isSwitched;
    }
    private void animateFade(VBox pane) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), pane);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void goToForgotPass(ActionEvent event) {
        try {
            Stage stage = (Stage) signInEmail.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/ForgotPassword.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            invalidLogin.setText("Erreur lors du chargement de la page de réinitialisation.");
        }
    }
}
