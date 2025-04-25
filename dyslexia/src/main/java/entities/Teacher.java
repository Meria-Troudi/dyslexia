package entities;

import java.util.Date;

public class Teacher extends User{
    private String subjectArea;
    private String availability;

    public Teacher() {
        super();
        this.role=Roles.ROLE_TEACHER;
    }

    public Teacher(int idUser, Roles role, String firstName, String lastName, Date birthDay, String gender, String profileImage, String email, String password,
                   Date dateCreation, String authCode, String subjectArea, String availability) {
        super(idUser, role, firstName, lastName, birthDay, gender, profileImage, email, password, dateCreation, authCode);
        this.subjectArea = subjectArea;
        this.availability = availability;
    }

    public String getSubjectArea() {return subjectArea;}
    public void setSubjectArea(String subjectArea) {this.subjectArea = subjectArea;}

    public String getAvailability() {return availability;}
    public void setAvailability(String availability) {this.availability = availability;}
public String toString() {
        return super.toString()+"Teacher{" +
                "subjectArea=" + subjectArea +
                ", availability=" + availability + '}';
}
}
