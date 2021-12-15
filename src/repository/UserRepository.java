package repository;

import bdd.SingletonConnection;
import models.User;

import java.sql.*;
import java.util.ArrayList;

public class UserRepository implements IRepository<User, String> {
    public final static UserRepository userRepository = new UserRepository();

    private UserRepository() {
    }

    @Override
    public User save(User object) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement createStmt = conn.prepareStatement("INSERT INTO User (pseudo) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            createStmt.setString(1, object.getPseudo());
            createStmt.executeUpdate();
            return get(object.getPseudo());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User get(String key) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from User u where pseudo = ? LIMIT 1");
            pstmt.setString(1, key);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) {
                return null;
            }

            User u = new User(res.getString("pseudo"));
            u.setKey(res.getInt(key));

            u.getManyToManyReferences().put("servers", new ArrayList<>());

            PreparedStatement stmt = conn.prepareStatement("SELECT * from Server JOIN Server_has_User ShU on Server.idServer = ShU.Server_idServer JOIN User U on U.pseudo = ShU.User_pseudo where pseudo = ?");
            stmt.setString(1, key);
            ResultSet resServer = stmt.executeQuery();

            while (resServer.next()) {
                u.getManyToManyReferences().get("servers").add(resServer.getInt("idServer"));
            }

            return u;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean delete(String key) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM User WHERE pseudo = ?");
            pstmt.setString(1, key);
            pstmt.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public User update(User object) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("UPDATE User  SET pseudo = ? WHERE pseudo = ?");
            pstmt.setString(1, object.getPseudo());
            pstmt.setString(2, object.getPseudo());
            pstmt.execute();

            return get(object.getPseudo());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<User> list() {
        ArrayList<User> users = new ArrayList<>();

        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from User u");
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                User u = new User(res.getString("pseudo"));
                u.setKey(res.getString("pseudo"));

                u.getManyToManyReferences().put("servers", new ArrayList<>());

                PreparedStatement stmt = conn.prepareStatement("SELECT * from Server Join Server_has_User ShU on Server.idServer = ShU.Server_idServer JOIN User U on U.pseudo = ShU.User_pseudo where pseudo = ?");
                stmt.setString(1, res.getString("pseudo"));
                ResultSet resServer = stmt.executeQuery();

                while (resServer.next()) {
                    u.getManyToManyReferences().get("servers").add(resServer.getInt("idServer"));
                }

                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
