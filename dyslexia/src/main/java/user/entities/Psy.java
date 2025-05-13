package user.entities;

import java.util.Date;

public class Psy extends User{
    private String schedule;           // e.g., "Mon-Wed 10-14h", for booking
    private String biography;

    public String getSchedule() {
        return schedule;
    }
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }

public Psy(){
        super();
}
    public Psy(int idUser, Roles role, String firstName, String lastName, Date birthDay, String gender, String profileImage, String email, int phoneNumber, String password,
               Date dateCreation, boolean isBanned, String schedule, String biography) {
        super(idUser, role, firstName, lastName, birthDay, gender, profileImage, email, phoneNumber, password, dateCreation, isBanned);
        this.schedule = schedule;
        this.biography = biography;
    }
}
