package user.entities;
import java.util.Date;

public class User {
    protected int idUser;
    protected Roles role;
    protected String firstName;
    protected String lastName;
    protected Date birthDay;
    protected String gender;
    protected String profileImage;
    protected String email;
    protected int phoneNumber;
    protected String password;
    protected Date dateCreation;
    protected boolean isBanned;
    private boolean enabled; // 🔥 ajouté ici
    private String resetToken;


    public User() {}

    public User(int idUser, Roles role, String firstName, String lastName,
                Date birthDay, String gender, String profileImage, String email,int phoneNumber, String password, Date dateCreation, boolean isBanned) {
        this.idUser = idUser;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.gender = gender;
        this.profileImage = profileImage;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.dateCreation = dateCreation;
        this.isBanned = isBanned;

    }
    @Override
    public String toString() {
        return "ID: " + idUser +
                ", Role: " + role +
                ", Name: " + firstName + " " + lastName;
    }

    public int getIdUser() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    public Roles getRole() {
        return role;
    }
    public void setRole(Roles role) {
        this.role = role;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {return password;}
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Date getBirthDay() {
        return birthDay;
    }
    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public Date getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(int phoneNumber) {this.phoneNumber = phoneNumber;}
    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

}
