package entities;

import java.util.Date;

public class DyslexicStudent extends User {
    private DyslexiaType dyslexiaType;
    private SeverityLevel severityLevel;
    private String specificNeeds;
    private String globalProgress;

    public DyslexicStudent() {
        super();
        this.role = Roles.ROLE_STUDENT;

    }

    public DyslexicStudent(int idUser, Roles role, String firstName, String lastName, Date birthDay, String gender, String profileImage, String email, String password, Date dateCreation, String authCode,
                           DyslexiaType dyslexiaType, SeverityLevel severityLevel, String specificNeeds, String globalProgress, Integer parentId) {
        super(idUser, role, firstName, lastName, birthDay, gender, profileImage, email, password, dateCreation, authCode);
        this.dyslexiaType = dyslexiaType;
        this.severityLevel = severityLevel;
        this.specificNeeds = specificNeeds;
        this.globalProgress = globalProgress;
    }

    public DyslexiaType getDyslexiaType() {return dyslexiaType;}
    public void setDyslexiaType(DyslexiaType dyslexiaType) {this.dyslexiaType = dyslexiaType;}

    public SeverityLevel getSeverityLevel() {return severityLevel;}
    public void setSeverityLevel(SeverityLevel severityLevel) {this.severityLevel = severityLevel;}

    public String getSpecificNeeds() {return specificNeeds;}
    public void setSpecificNeeds(String specificNeeds) {this.specificNeeds = specificNeeds;}

    public String getGlobalProgress() {return globalProgress;}
    public void setGlobalProgress(String globalProgress) {this.globalProgress = globalProgress;}



    @Override
    public String toString() {
        return super.toString()  +
                "dyslexiaType=" + dyslexiaType +
                ", severityLevel=" + severityLevel +
                ", specificNeeds='" + specificNeeds + '\'' +
                ", globalProgress='" + globalProgress + '\'' +
                '}';
    }
}

