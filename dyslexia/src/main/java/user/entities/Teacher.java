package user.entities;

import java.util.Date;

public class Teacher extends User{
    private String subjectArea;
    private String availability;

    public Teacher() {
        super();
        this.role=Roles.ROLE_TEACHER;
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
