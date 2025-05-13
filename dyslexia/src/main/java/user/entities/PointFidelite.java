package user.entities;

public class PointFidelite {
    private int id;
    private int idUser;
    private int points;
    public PointFidelite() {}
    public PointFidelite(int id, int idUser, int points) {
        this.id = id;
        this.idUser = idUser;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

