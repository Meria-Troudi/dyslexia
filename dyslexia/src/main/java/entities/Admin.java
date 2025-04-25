package entities;

import java.sql.Timestamp;
import java.util.Date;

public class Admin extends User{
    private Timestamp lastLogin;

    public Admin() {
        super();
        this.role = Roles.ROLE_ADMIN;
    }

    public Admin(int idUser, Roles role, String firstName, String lastName, Date birthDay, String gender, String profileImage, String email, String password,
                 Date dateCreation, String authCode, Timestamp lastLogin) {
        super(idUser, role, firstName, lastName, birthDay, gender, profileImage, email, password, dateCreation, authCode);
        this.lastLogin = lastLogin;
    }

    public Timestamp getLastLogin() {return lastLogin;}

    public void setLastLogin(Timestamp lastLogin) {this.lastLogin = lastLogin;}

    @Override
    public String toString() {
        return "Admin{" +
                "lastLogin=" + lastLogin +
                '}';
    }
}
