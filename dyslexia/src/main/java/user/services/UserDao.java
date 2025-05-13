package user.services;
import utils.DatabaseConnection;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import user.services.session.UserSession;
import user.entities.*;
public class UserDao {

    private Connection cnx;
    private PreparedStatement ps;
    private final UserServiceImp serviceUser = new UserServiceImp();
    private final OTPService otpService = new OTPService();

    public UserDao() {
        cnx = DatabaseConnection.getInstance().getConnection();
    }
    public void updateProfileImage(int userId, String base64Image) throws SQLException {
        String sql = "UPDATE users SET profileImage = ? WHERE idUser = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, base64Image);
            ps.setInt   (2, userId);
            ps.executeUpdate();
        }
    }
    public String getHashedPasswordByUsername(String email) throws SQLException {
        String hashedPassword = null;
        if (cnx == null || cnx.isClosed()) {
            cnx = DatabaseConnection.getInstance().getConnection();
        }String sql = "SELECT password FROM users WHERE email = ?";
        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {hashedPassword = rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération du mot de passe : " + e.getMessage());
            throw e;
        }return hashedPassword;
    }
    public boolean login(String email, String password) throws SQLException {
        // 1) verify password
        String hash = getHashedPasswordByUsername(email);
        if (hash == null || !BCrypt.checkpw(password, hash)) return false;

        // 2) fetch user + role-specific fields in one go
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;

                // common
                Roles role = Roles.valueOf(rs.getString("role"));
                User u;

                // subtype
                if (role == Roles.ROLE_STUDENT) {
                    DyslexicStudent ds = new DyslexicStudent();
                    ds.setDyslexiaType(DyslexiaType.valueOf(rs.getString("dyslexiaType")));
                    ds.setSeverityLevel(SeverityLevel.valueOf(rs.getString("severityLevel")));
                    ds.setSpecificNeeds(rs.getString("specificNeeds"));
                    u = ds;

                } else if (role == Roles.ROLE_TEACHER) {
                    Teacher t = new Teacher();
                    t.setSubjectArea(rs.getString("subjectArea"));
                    t.setAvailability(rs.getString("availability"));
                    u = t;

                } else if (role == Roles.ROLE_PSY) {
                    Psy p = new Psy();
                    p.setBiography(rs.getString("biography"));
                    p.setSchedule(rs.getString("schedule"));
                    u = p;

                } else {
                    u = new User();
                }

                // fill common fields
                u.setIdUser(rs.getInt("idUser"));
                u.setEmail(email);
                u.setPassword(hash);
                u.setFirstName(rs.getString("firstName"));
                u.setLastName(rs.getString("lastName"));
                u.setBirthDay(rs.getDate("birthDay"));
                u.setGender(rs.getString("gender"));
                u.setProfileImage(rs.getString("profileImage"));
                u.setPhoneNumber(rs.getInt("phoneNumber"));
                u.setDateCreation(rs.getTimestamp("dateCreation"));
                u.setRole(role);
                u.setBanned(rs.getBoolean("isBanned"));
                u.setEnabled(rs.getBoolean("enabled"));
                u.setResetToken(rs.getString("resetToken"));

                // 3) store & done
                if (u.isBanned()) return false;
                UserSession.setCurrentUser(new UserSession(u));
                updateLastLogin(u.getIdUser());
                return true;
            }
        }
    }


    private void updateLastLogin(int userId) {
        String sql = "UPDATE users SET lastLogin = ? WHERE idUser = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Set current time
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
        }
    }

    public void changePassword(User user) throws SQLException {
        if (cnx == null || cnx.isClosed()) {
            cnx = DatabaseConnection.getInstance().getConnection();
        }
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, user.getPassword()); // must be hashed already!
            ps.setString(2, user.getEmail());
            ps.executeUpdate();
        }
    }
    public void changePasswordByEmail(String email, String hashedPassword) throws SQLException {
        // make sure connection is open
        if (cnx == null || cnx.isClosed()) {
            cnx = DatabaseConnection.getInstance().getConnection();
        }
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }
    public void banUser(int userId) throws SQLException {
        String query = "UPDATE users SET isBanned = TRUE WHERE idUser = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, userId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur banni avec succès.");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        }
    }

    /**
     * Active un utilisateur après confirmation par email.
     */
    public boolean enableUser(String email) throws SQLException {
        String sql = "UPDATE users SET enabled = TRUE WHERE email = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, email);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'activation de l'utilisateur : " + e.getMessage());
            throw e;
        }
    }

    // ✅ Utilitaire pour mapper un ResultSet à un objet User
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
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

        if (hasColumn(rs, "resetToken")) {
            user.setResetToken(rs.getString("resetToken"));
        }
        return user;
    }

    public String getConfirmationTokenByEmail(String email) {
        String token = null;
        String sql = "SELECT resetToken FROM users WHERE email = ?"; // Fixed table name

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    token = rs.getString("resetToken");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du token : " + e.getMessage());
        }

        return token;
    }
    // ✅ Vérifie si une colonne existe (utile si "resetToken" est parfois absent)
    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    // ✅ Trouver un utilisateur par email
    public User findByEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par email : " + e.getMessage());
            throw e;
        }
        return null;
    }
}





