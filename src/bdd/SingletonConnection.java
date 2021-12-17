package bdd;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    public final static Connection connection = SingletonConnection.getConnection();

    private SingletonConnection() {
    }

    private static Connection getConnection() {
        try {
            String password = Files.readString(Path.of("./password.txt"), StandardCharsets.UTF_8);
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/Slake?user=root&password=" + password);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
