package user.services;
import user.entities.*;
import jakarta.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import user.services.session.*;
import utils.DatabaseConnection;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class UserServiceImp implements UserService {
    private Connection connection;
    public UserServiceImp() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }}
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }return false;
    }
    // Génère un token UUID (restToken)
    private String generateRestToken() {
        return UUID.randomUUID().toString();
    }
    public void createUser(User u){
        String restToken = generateRestToken();  // Utiliser le resetToken ici
        String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
        u.setPassword(hashedPassword);

        try {
            String sql = "INSERT INTO users (role, email, password, firstName, lastName, birthDay, gender, profileImage, phoneNumber, " +
                    "dateCreation, isBanned, enabled, resetToken, lastLogin, " +
                    "subjectArea, availability, dyslexiaType, severityLevel, specificNeeds, globalProgress,schedule, biography) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, "ROLE_ADMIN");
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getPassword());
                ps.setString(4, u.getFirstName());
                ps.setString(5, u.getLastName());
                ps.setDate(6, new java.sql.Date(u.getBirthDay().getTime()));
                ps.setString(7, u.getGender());
                ps.setString(8, u.getProfileImage());
                ps.setInt(9, u.getPhoneNumber());
                ps.setTimestamp(10, new Timestamp(u.getDateCreation().getTime()));
                ps.setBoolean(11, false); // pas banni
                ps.setBoolean(12, false); // pas encore activé
                ps.setString(13, restToken);
                // Set defaults (nulls) for role-specific fields
                ps.setNull(14, Types.TIMESTAMP); // lastLogin
                ps.setNull(15, Types.VARCHAR);   // subjectArea
                ps.setNull(16, Types.VARCHAR);   // availability
                ps.setNull(17, Types.VARCHAR);   // dyslexiaType
                ps.setNull(18, Types.VARCHAR);   // severityLevel
                ps.setNull(19, Types.VARCHAR);   // specificNeeds
                ps.setNull(20, Types.VARCHAR);   // globalProgress
                ps.setNull(21, Types.VARCHAR);   // specificNeeds
                ps.setNull(22, Types.VARCHAR);   // globalProgress
                if (u instanceof Admin admin) {
                    ps.setTimestamp(14, admin.getLastLogin());
                } else if (u instanceof DyslexicStudent student) {
                    ps.setString(17, student.getDyslexiaType() != null ? student.getDyslexiaType().toString() : null);
                    ps.setString(18, student.getSeverityLevel() != null ? student.getSeverityLevel().toString() : null);
                    ps.setString(19, student.getSpecificNeeds());
                    ps.setString(20, student.getGlobalProgress());
                }else if (u instanceof Teacher teacher) {
                    ps.setString(15, teacher.getSubjectArea());
                    ps.setString(16, teacher.getAvailability());
                }else if (u instanceof Psy psy) {
                    ps.setString(21, psy.getBiography());
                    ps.setString(22, psy.getSchedule());
                }
                ps.executeUpdate();
                // Fetch generated ID
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    u.setIdUser(generatedId);
                } else {
                    throw new SQLException("❌ User created but no ID returned.");
                }
            }
            } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }// Envoyer le token de réinitialisation (resetToken) par email
        sendConfirmationEmail(u, restToken);  // Utiliser resetToken ici
    }
    // Envoi du mail avec le resetToken
    private void sendConfirmationEmail(User user, String resetToken) {
        String subject = "Votre code de confirmation";
        String body = "Bonjour " + user.getFirstName() + ",\n\n" +
                "Merci pour votre inscription. Voici votre code de confirmation :\n\n" +
                "**" + resetToken + "**\n\n" + // Utiliser resetToken dans l'email
                "Veuillez entrer ce code dans l'application pour activer votre compte.\n\n" +
                "Cordialement,\nL'équipe.";
        try {
            MailService.send(user.getEmail(), subject, body);  // Envoi du mail avec MailService
        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi du mail de confirmation : " + e.getMessage());
        }
    }
    public boolean confirmAccount(String confirmationCode) {
        try {
            String sql = "SELECT * FROM users WHERE resetToken = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, confirmationCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String updateSql = "UPDATE users SET enabled = TRUE, resetToken = NULL WHERE resetToken = ?";
                PreparedStatement updatePs = connection.prepareStatement(updateSql);
                updatePs.setString(1, confirmationCode);
                updatePs.executeUpdate();
                return true; // Compte activé
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la confirmation du compte : " + e.getMessage());
        }
        return false; // Code invalide ou autre erreur
    }

    public void removeUSER(int id){
        try {
            String sql = "DELETE FROM users WHERE idUser = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error removing user: " + e.getMessage());
        }
    }
    public void updateUser(User user) {
        String query = "SELECT password FROM users WHERE idUser = ?";
        String updateSQL = "UPDATE users SET email=?, password=?, firstName=?, lastName=?, birthDay=?, gender=?, " +
                "profileImage=?, phoneNumber=?, isBanned=?, enabled=?, resetToken=?, lastLogin=?, subjectArea=?, availability=?, " +
                "dyslexiaType=?, severityLevel=?, specificNeeds=?, globalProgress=? WHERE idUser=?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, user.getIdUser());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String existingPassword = rs.getString("password");

                if (user.getPassword() != null && !user.getPassword().isEmpty() && !BCrypt.checkpw(user.getPassword(), existingPassword)) {
                    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
                } else {
                    user.setPassword(existingPassword);
                }

                try (PreparedStatement updatePs = connection.prepareStatement(updateSQL)) {
                    updatePs.setString(1, user.getEmail());
                    updatePs.setString(2, user.getPassword());
                    updatePs.setString(3, user.getFirstName());
                    updatePs.setString(4, user.getLastName());
                    updatePs.setDate(5, new java.sql.Date(user.getBirthDay().getTime()));
                    updatePs.setString(6, user.getGender());
                    updatePs.setString(7, user.getProfileImage());
                    updatePs.setInt(8, user.getPhoneNumber());
                    updatePs.setBoolean(9, user.isBanned());
                    updatePs.setBoolean(10, user.isEnabled());
                    updatePs.setString(11, user.getResetToken());
                    // Initialize all optional fields as null
                    updatePs.setNull(12, Types.TIMESTAMP); // lastLogin
                    updatePs.setNull(13, Types.VARCHAR);   // subjectArea
                    updatePs.setNull(14, Types.VARCHAR);   // availability
                    updatePs.setNull(15, Types.VARCHAR);   // dyslexiaType
                    updatePs.setNull(16, Types.VARCHAR);   // severityLevel
                    updatePs.setNull(17, Types.VARCHAR);   // specificNeeds
                    updatePs.setNull(18, Types.VARCHAR);   // globalProgress
                    // Role-specific fields
                    if (user instanceof Admin admin) {
                        updatePs.setTimestamp(10, admin.getLastLogin());
                    } else if (user instanceof Teacher teacher) {
                        updatePs.setString(11, teacher.getSubjectArea());
                        updatePs.setString(12, teacher.getAvailability());
                    } else if (user instanceof DyslexicStudent student) {
                        updatePs.setString(13, student.getDyslexiaType().toString());
                        updatePs.setString(14, student.getSeverityLevel().toString());
                        updatePs.setString(15, student.getSpecificNeeds());
                        updatePs.setString(16, student.getGlobalProgress());
                    } else if (user instanceof Psy psy) {
                        updatePs.setString(13, psy.getBiography());
                        updatePs.setString(14, psy.getSchedule());
                    }

                    updatePs.setInt(19, user.getIdUser());
                    updatePs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }

    }
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                User user = createUserFromResultSet(rs);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving users: " + e.getMessage());
        }
        return users;
}
    public User getUserById(int id){
        String sql = "SELECT * FROM users WHERE idUser = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            //runs the query and stores the result (rows) in rs.
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? createUserFromResultSet(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            return null;
        }
    }
    public User getUserByEmail(String email){
        String sql ="SELECT * FROM users WHERE email = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            return null;
        }
        return null;
    }

    public  User createUserFromResultSet(ResultSet rs) throws SQLException {
        Roles role = Roles.valueOf(rs.getString("role"));
        User user = switch (role) {
            case ROLE_STUDENT -> createDyslexicStudent(rs);
            case ROLE_TEACHER -> createTeacher(rs);
            case ROLE_ADMIN -> createAdmin(rs);
            case ROLE_PSY -> createPsy(rs);
        };
        if (user != null) {
            //commonFields
            user.setIdUser(rs.getInt("idUser"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setBirthDay(rs.getDate("birthDay"));
            user.setGender(rs.getString("gender"));
            user.setProfileImage(rs.getString("profileImage"));
            user.setPhoneNumber(rs.getInt("phoneNumber"));
            user.setDateCreation(rs.getTimestamp("dateCreation"));
            user.setRole(Roles.valueOf(rs.getString("role")));
            user.setBanned(rs.getBoolean("isbanned"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setResetToken(rs.getString("resetToken"));
        }
        return user;

    }
    private DyslexicStudent createDyslexicStudent(ResultSet rs) throws SQLException {
        DyslexicStudent student = new DyslexicStudent();
        String dyslexiaType = rs.getString("dyslexiaType");
        String severityLevel = rs.getString("severityLevel");
        if (dyslexiaType != null) student.setDyslexiaType( DyslexiaType.valueOf(rs.getString("dyslexiaType")));
        if (severityLevel != null) student.setSeverityLevel(SeverityLevel.valueOf(rs.getString("severityLevel")));
        student.setSpecificNeeds(rs.getString("specificNeeds"));
        student.setGlobalProgress(rs.getString("globalProgress"));
        return student;
    }
    private Teacher createTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setSubjectArea(rs.getString("subjectArea"));
        teacher.setAvailability(rs.getString("availability"));
        return teacher;
    }
    private Admin createAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setLastLogin(rs.getTimestamp("lastLogin"));
        return admin;
    }

    private Psy createPsy(ResultSet rs) throws SQLException {
        Psy psy = new Psy();

        psy.setResetToken(rs.getString("biography"));
        psy.setResetToken(rs.getString("schedule"));


        return psy;
    }
    // Méthode pour créer l'admin par défaut avec le rôle ROLE_ADMIN
    public void createDefaultAdmin() {
        final String email = "admin@gmail.com";
        final String rawPw = "admin123";
        final String lookup = "SELECT idUser FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(lookup)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                String newHash = BCrypt.hashpw(rawPw, BCrypt.gensalt(10));
                if (rs.next()) {
                    // admin exists → reset its password & lastLogin
                    try (PreparedStatement upd = connection.prepareStatement(
                            "UPDATE users SET password = ?, lastLogin = ? WHERE email = ?"
                    )) {
                        upd.setString(1, newHash);
                        upd.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        upd.setString(3, email);
                        upd.executeUpdate();
                    }
                } else {
                    // no admin yet → insert one
                    Admin admin = new Admin();
                    admin.setEmail(email);
                    admin.setPassword(rawPw);      // raw, createUser() will hash it
                    admin.setFirstName("Default");
                    admin.setLastName("Admin");
                    admin.setGender("Other");
                    admin.setBirthDay(new java.util.Date());
                    admin.setEnabled(true);
                    admin.setProfileImage("default_admin.png");
                    admin.setPhoneNumber(12345678);
                    admin.setDateCreation(new java.util.Date());
                    admin.setRole(Roles.ROLE_ADMIN);
                    admin.setLastLogin(new Timestamp(System.currentTimeMillis()));

                    createUser(admin);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private String generateResetToken() {
        return UUID.randomUUID().toString();  // Génère un token unique
    }
    private void saveResetTokenForUser(User user, String resetToken) {
        // Implémentation de la sauvegarde du token pour l'utilisateur
        // Par exemple, mettre à jour l'utilisateur avec le resetToken dans la table users
        user.setResetToken(resetToken);
    }



    public DyslexicStudent getDyslexicStudentById(int id) {
        String query = "SELECT * FROM users WHERE idUser = ? AND role = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.setString(2, Roles.ROLE_STUDENT.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                DyslexicStudent student = new DyslexicStudent();
                student.setIdUser(rs.getInt("idUser"));
                student.setFirstName(rs.getString("firstName"));
                student.setLastName(rs.getString("lastName"));
                student.setEmail(rs.getString("email"));
                // Remplir tous les champs spécifiques :
                student.setSpecificNeeds(rs.getString("specificNeeds"));
                student.setDyslexiaType(DyslexiaType.valueOf(rs.getString("dyslexiaType")));
                student.setSeverityLevel(SeverityLevel.valueOf(rs.getString("severityLevel")));
                student.setGlobalProgress(rs.getString("globalProgress"));
                // etc.
                return student;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de DyslexicStudent : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}




