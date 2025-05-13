package event.Events_Attributs;

public class Events{
   private int id_events ;
    private String title;
    private String description;
    private String date_events;
    private String time ;
    private String location;
    private String type;
    private int id_admin ;
    private String photo;
    public Events() {
    }
    public Events(String title, String description, String date_events, String time, String location, String type) {
        this.title = title;
        this.description = description;
        this.date_events = date_events;
        this.time = time;
        this.location = location;
        this.type = type;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Events(int id_events, String title, String description, String date_events, String time, String location, String type, int id_admin) {
        this.id_events = id_events;
        this.title = title;
        this.description = description;
        this.date_events = date_events;
        this.time = time;
        this.location = location;
        this.type = type;
        this.id_admin = id_admin;
    }

    public Events(String title, String description, String date_events, String time, String location, String type, int id_admin) {
        this.title = title;
        this.description = description;
        this.date_events = date_events;
        this.time = time;
        this.location = location;
        this.type = type;
        this.id_admin = id_admin;
    }



    public int getId_events() {
        return id_events;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate_events() {
        return date_events;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_events(int id_events) {
        this.id_events = id_events;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate_events(String date_events) {
        this.date_events = date_events;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    @Override
    public String toString() {
        return "Events{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date_events='" + date_events + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getPhoto() {
        return photo;
    }

    public Events(String title, String description, String date_events, String time, String location, String type, String photo) {
        this.title = title;
        this.description = description;
        this.date_events = date_events;
        this.time = time;
        this.location = location;
        this.type = type;
        this.photo = photo;
    }
}
