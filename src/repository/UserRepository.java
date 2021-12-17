package repository;

import bdd.SingletonConnection;
import models.Server;
import models.User;

import java.sql.*;
import java.util.ArrayList;

public class UserRepository implements IRepository<User, String> {
    public final static UserRepository userRepository = new UserRepository();

    private UserRepository() {
    }

    @Override
    public User save(User user) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement createStmt = conn.prepareStatement(
                    "INSERT INTO User (username, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            createStmt.setString(1, user.getUsername());
            createStmt.setString(2, user.getPassword());
            createStmt.executeUpdate();
            return get(user.getUsername());
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
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from User u where username = ? LIMIT 1");
            pstmt.setString(1, key);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) {
                return null;
            }

            User u = new User(res.getString("username"), res.getString("password"));
            u.setKey(res.getString("username"));

            u.getManyToManyReferences().put("servers", new ArrayList<>());

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * from Server JOIN Server_has_User ShU on Server.idServer = ShU.Server_idServer JOIN User U on U.username = ShU.User_username where username = ?");
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
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM User WHERE username = ?");
            pstmt.setString(1, key);
            pstmt.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public User update(User user) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("UPDATE User SET username = ?, password = ? WHERE username = ?");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getUsername());
            pstmt.execute();

            return get(user.getUsername());
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
                User u = new User(res.getString("username"), res.getString("password"));
                u.setKey(res.getString("username"));

                u.getManyToManyReferences().put("servers", new ArrayList<>());

                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * from Server Join Server_has_User ShU on Server.idServer = ShU.Server_idServer JOIN User U on U.username = ShU.User_username where username = ?");
                stmt.setString(1, res.getString("username"));
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

    public User addServer(User user, Server server) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement createStmt = conn.prepareStatement(
                    "INSERT INTO Server_has_User (Server_idServer, User_username) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            createStmt.setInt(1, (Integer) server.getKey());
            createStmt.setString(2, (String) user.getKey());
            createStmt.executeUpdate();
            return get((String) user.getKey());
        } catch (SQLException ignored) {
        }
        return get((String) user.getKey());
    }
}
