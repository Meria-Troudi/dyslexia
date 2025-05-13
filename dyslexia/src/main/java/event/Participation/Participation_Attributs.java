package event.Participation;

//import java.security.Timestamp;
import java.sql.Timestamp;
public class Participation_Attributs {
    private int id_participation;
    private int id_events;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String commentaire;
    private Timestamp date_participation;

    // Constructeur vide


    public Participation_Attributs() {
    }

    // Constructeur avec tous les champs (sauf id auto-incrémenté et date automatique)
    public Participation_Attributs(int id_events, String nom, String prenom, String email, String telephone, String commentaire) {
        this.id_events = id_events;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.commentaire = commentaire;
    }

    // Getters et Setters
    public int getId_participation() {
        return id_participation;
    }

    public void setId_participation(int id_participation) {
        this.id_participation = id_participation;
    }

    public int getId_events() {
        return id_events;
    }

    public void setId_events(int id_events) {
        this.id_events = id_events;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Timestamp getDate_participation() {
        return date_participation;
    }

    public void setDate_participation(Timestamp date_participation) {
        this.date_participation = date_participation;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "id_participation=" + id_participation +
                ", id_events=" + id_events +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", date_participation=" + date_participation +
                '}';
    }






}
