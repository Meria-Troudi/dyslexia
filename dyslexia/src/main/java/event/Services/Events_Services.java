package event.Services;

import event.Events_Attributs.Events;
import event.events.MyDB;
import  java.time.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Events_Services implements I_Services<Events> {
Connection conn ;

    public Events_Services() {
        this.conn = MyDB.getInstance().getConn();
    }

   /* public Events_Services() {

    }*/

    @Override
    public void Create(Events events) throws Exception{
        String sql = "INSERT INTO events (id_events, title, description, date_events, time, location, type, id_admin, photo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, events.getId_events());
            pstmt.setString(2, events.getTitle());
            pstmt.setString(3, events.getDescription());
            pstmt.setString(4, events.getDate_events());  // Format "YYYY-MM-DD"
            pstmt.setString(5, events.getTime());        // Format "HH:MM:SS"
            pstmt.setString(6, events.getLocation());
            pstmt.setString(7, events.getType());
            pstmt.setInt(8, events.getId_admin());
            pstmt.setString(9, events.getPhoto()); // Assurez-vous que tu as le bon attribut photo dans la classe Events

            pstmt.executeUpdate();
        }}

    @Override
    public void Update(Events events) throws Exception {

            String req = "UPDATE events SET title = ?, description = ?, date_events = ?, time = ?, location = ?, type = ?, id_admin = ? WHERE id_events = ?";
            PreparedStatement stmt = conn.prepareStatement(req);
            stmt.setString(1, events.getTitle());
            stmt.setString(2, events.getDescription());
            stmt.setString(3, events.getDate_events());
            stmt.setString(4, events.getTime());
            stmt.setString(5, events.getLocation());
            stmt.setString(6, events.getType());
            stmt.setInt(7, events.getId_admin());
            stmt.setInt(8, events.getId_events());
            stmt.executeUpdate();

    }

    @Override
    public List<Events> Display() throws Exception {

        String req = "select * from events ";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(req);
        List<Events> eventsList = new ArrayList<>();
        while (rs.next()){
            Events e1 = new Events();
            e1.setId_events(rs.getInt("id_events"));
            e1.setTitle(rs.getString("title"));
            e1.setDescription(rs.getString("description"));
            e1.setDate_events(rs.getString("Date_events"));
            e1.setTime(rs.getString("time"));
            e1.setLocation(rs.getString("location"));
            e1.setType(rs.getString("type"));
            e1.setId_admin(rs.getInt("id_admin"));
            e1.setPhoto(rs.getString("photo"));
            eventsList.add(e1);
        }
        return eventsList;
    }

    @Override
    public void Delete(int id_events) throws Exception {
        String req = "DELETE FROM events WHERE id_events = ?";
        PreparedStatement stmt = conn.prepareStatement(req);
        stmt.setInt(1, id_events);
        stmt.executeUpdate();
        stmt.close();}

        public void deleteEvent(int eventId)throws SQLException {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/events_participation");
            }
            String query = "DELETE FROM events WHERE id_events = ?";
            try (Connection conn = MyDB.getInstance().getConn();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                // Lier l'ID de l'événement à la requête
                ps.setInt(1, eventId);

                // Exécuter la requête
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Événement supprimé avec succès.");
                } else {
                    System.out.println("Aucun événement trouvé avec cet ID.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }





