package event.Services;

import event.Participation.Participation_Attributs;
import event.events.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
public class Participation_Services implements P_Services<Participation_Attributs> {
    Connection conn;

    public Participation_Services() {
        this.conn = MyDB.getInstance().getConn();
    }

    @Override
    public void Create(Participation_Attributs p) throws Exception {
        String sql = "INSERT INTO participation (id_events, nom, prenom, email, telephone, commentaire, date_participation) "
                + "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, p.getId_events());
            pstmt.setString(2, p.getNom());
            pstmt.setString(3, p.getPrenom());
            pstmt.setString(4, p.getEmail());
            pstmt.setString(5, p.getTelephone());
            pstmt.setString(6, p.getCommentaire());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void Update(Participation_Attributs p) throws Exception {
        String req = "UPDATE participation SET id_events = ?, nom = ?, prenom = ?, email = ?, telephone = ?, commentaire = ? WHERE id_participation = ?";

        try (PreparedStatement stmt = conn.prepareStatement(req)) {
            stmt.setInt(1, p.getId_events());
            stmt.setString(2, p.getNom());
            stmt.setString(3, p.getPrenom());
            stmt.setString(4, p.getEmail());
            stmt.setString(5, p.getTelephone());
            stmt.setString(6, p.getCommentaire());
            stmt.setInt(7, p.getId_participation());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Participation_Attributs> Display() throws Exception {
        List<Participation_Attributs> list = new ArrayList<>();
        String req = "SELECT * FROM participation WHERE id_events = ?";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(req)) {

            while (rs.next()) {
                Participation_Attributs p = new Participation_Attributs();
                p.setId_participation(rs.getInt("id_participation"));
                p.setId_events(rs.getInt("id_events"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setEmail(rs.getString("email"));
                p.setTelephone(rs.getString("telephone"));
                p.setCommentaire(rs.getString("commentaire"));
                p.setDate_participation(rs.getTimestamp("date_participation"));

                list.add(p);
            }
        }
        return list;
    }

    @Override
    public void Delete(int id_participation) throws Exception {
        String req = "DELETE FROM participation WHERE id_participation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(req)) {
            stmt.setInt(1, id_participation);
            stmt.executeUpdate();
        }
    }



    public boolean isEventExists(int eventId) {
        String query = "SELECT COUNT(*) FROM events WHERE id_events = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Participation_Attributs> DisplayByEvent(int idEvent) throws SQLException {
        List<Participation_Attributs> list = new ArrayList<>();

        String query = "SELECT * FROM participation WHERE id_events = ?";
        Connection conn = MyDB.getInstance().getConn();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, idEvent);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Participation_Attributs p = new Participation_Attributs();
            p.setNom(rs.getString("nom"));
            p.setPrenom(rs.getString("prenom"));
            p.setEmail(rs.getString("email"));
            p.setTelephone(rs.getString("telephone"));
            p.setCommentaire(rs.getString("commentaire"));
            Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());
            p.setDate_participation(dateActuelle);            // ajoute d'autres champs si nécessaire
            list.add(p);
        }

        return list;
    }





}
