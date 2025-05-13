package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private final String URL = "jdbc:mysql://localhost:3306/project_db";
    private final String USER ="root";
    private final String PWD ="";
    private Connection cnx;

    //Creer une instance static de meme type que la classe
    private static DatabaseConnection instance;

    private DatabaseConnection(){
        try {
            cnx = DriverManager.getConnection(URL,USER,PWD);
            System.out.println("Connected!!!");
        } catch (SQLException e) {
            System.out.println("Error:"+e.getMessage());
        }
    }
    // Creer une methode static pour recuperer l'instance
    public static DatabaseConnection getInstance() {
        if(instance == null)
            instance = new DatabaseConnection();
        return instance;
    }

    public Connection getConnection() {
        return cnx;
    }
}
