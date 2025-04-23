package utiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String URL = "jdbc:mysql://localhost:3306/dys";
    private final String USER = "root";
    private final String PSW = "";
    private Connection connection;

    private static DatabaseConnection instance;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PSW);
            System.out.println("Connected!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null)
            instance = new DatabaseConnection();
        return instance;
    }
}