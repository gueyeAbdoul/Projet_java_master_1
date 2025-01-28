package connexionJavaSql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBasedeDonnee {

    public static Connection connection;

    static{
        try{
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","702054981Ag@");
            System.out.println("CONNECTED");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("DB Connection failed !");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection(){
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "702054981Ag@");
                System.out.println("Reconnected to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to reconnect to the database.", e);
        }
        return connection;
    }

}
