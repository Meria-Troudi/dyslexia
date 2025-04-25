package services;
import entities.*;
import utiles.DatabaseConnection;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class UserServiceImp implements UserService {
    private Connection connection;
    public UserServiceImp() {
        connection = DatabaseConnection.getInstance().getConnection();
    }
    public void createUser(User u){
        try {
            String sql = "INSERT INTO users (role, email, password, firstName, lastName, birthDay, gender, profileImage, " +
                    "dateCreation, authCode, lastLogin, subjectArea, availability, dyslexiaType, severityLevel, specificNeeds, globalProgress) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, u.getRole().toString());
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getPassword());
                ps.setString(4, u.getFirstName());
                ps.setString(5, u.getLastName());
                ps.setDate(6, new java.sql.Date(u.getBirthDay().getTime()));
                ps.setString(7, u.getGender());
                ps.setString(8, u.getProfileImage());
                ps.setTimestamp(9, new Timestamp(u.getDateCreation().getTime()));
                ps.setString(10, u.getAuthCode());

                // Set defaults (nulls) for role-specific fields
                ps.setNull(11, Types.TIMESTAMP); // lastLogin
                ps.setNull(12, Types.VARCHAR);   // subjectArea
                ps.setNull(13, Types.VARCHAR);   // availability
                ps.setNull(14, Types.VARCHAR);   // dyslexiaType
                ps.setNull(15, Types.VARCHAR);   // severityLevel
                ps.setNull(16, Types.VARCHAR);   // specificNeeds
                ps.setNull(17, Types.VARCHAR);   // globalProgress

                if (u instanceof Admin admin) {
                    ps.setTimestamp(11, admin.getLastLogin());
                } else if (u instanceof DyslexicStudent student) {
                    ps.setString(14, student.getDyslexiaType().toString());
                    ps.setString(15, student.getSeverityLevel().toString());
                    ps.setString(16, student.getSpecificNeeds());
                    ps.setString(17, student.getGlobalProgress());
                }else if (u instanceof Teacher teacher) {
                    ps.setString(12, teacher.getSubjectArea());
                    ps.setString(13, teacher.getAvailability());
                }

                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    u.setIdUser( generatedKeys.getInt(1));
                }
            }

            } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
        }


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
        try {
            String sql = "UPDATE users SET email=?, password=?, firstName=?, lastName=?, birthDay=?, " +
                    "gender=?, profileImage=?, authCode=?, lastLogin=?, subjectArea=?, availability=?, " +
                    "dyslexiaType=?, severityLevel=?, specificNeeds=?, globalProgress=? " +
                    "WHERE idUser=?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                // Common fields
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getFirstName());
                ps.setString(4, user.getLastName());
                ps.setDate(5, new java.sql.Date(user.getBirthDay().getTime()));
                ps.setString(6, user.getGender());
                ps.setString(7, user.getProfileImage());
                ps.setString(8, user.getAuthCode());

                // Role-specific fields
                if (user instanceof Admin admin) {
                    ps.setTimestamp(9, admin.getLastLogin());
                    ps.setNull(10, Types.VARCHAR); // subjectArea
                    ps.setNull(11, Types.VARCHAR); // availability
                    ps.setNull(12, Types.VARCHAR); // dyslexiaType
                    ps.setNull(13, Types.VARCHAR); // severityLevel
                    ps.setNull(14, Types.VARCHAR); // specificNeeds
                    ps.setNull(15, Types.VARCHAR); // globalProgress
                } else if (user instanceof Teacher teacher) {
                    ps.setNull(9, Types.TIMESTAMP); // lastLogin
                    ps.setString(10, teacher.getSubjectArea());
                    ps.setString(11, teacher.getAvailability());
                    ps.setNull(12, Types.VARCHAR); // dyslexiaType
                    ps.setNull(13, Types.VARCHAR); // severityLevel
                    ps.setNull(14, Types.VARCHAR); // specificNeeds
                    ps.setNull(15, Types.VARCHAR); // globalProgress
                } else if (user instanceof DyslexicStudent student) {
                    ps.setNull(9, Types.TIMESTAMP); // lastLogin
                    ps.setNull(10, Types.VARCHAR); // subjectArea
                    ps.setNull(11, Types.VARCHAR); // availability
                    ps.setString(12, student.getDyslexiaType().toString());
                    ps.setString(13, student.getSeverityLevel().toString());
                    ps.setString(14, student.getSpecificNeeds());
                    ps.setString(15, student.getGlobalProgress());
                }

                ps.setInt(16, user.getIdUser());
                ps.executeUpdate();
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
            user.setDateCreation(rs.getTimestamp("dateCreation"));
            user.setAuthCode(rs.getString("authCode"));
            user.setRole(Roles.valueOf(rs.getString("role")));
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




}
