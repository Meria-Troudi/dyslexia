package event.events;
import java.sql.*;


public class MyDB {
    private Connection conn ;
    private String url = "jdbc:mysql://localhost:3306/project_db";
    private String user = "root";
    private String pwd ="";

    private static MyDB instance;

    public static MyDB getInstance() {
        if (instance == null){
            instance = new MyDB();
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }

    private MyDB() {
        try {
            this.conn = DriverManager.getConnection(url, user, pwd);
            System.out.println("cnx est établi ");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
