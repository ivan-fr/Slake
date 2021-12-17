package repository;

import bdd.SingletonConnection;
import models.Server;

import java.sql.*;
import java.util.ArrayList;

public class ServerRepository implements IRepository<Server, Integer> {
    public final static ServerRepository serverRepository = new ServerRepository();

    @Override
    public Server save(Server object) {
        Connection conn = SingletonConnection.connection;

        try {
            assert conn != null;
            PreparedStatement createStmt = conn.prepareStatement("INSERT INTO Server (name, userCounter) VALUES (?, 0)",
                    Statement.RETURN_GENERATED_KEYS);
            createStmt.setString(1, object.getName());
            createStmt.executeUpdate();
            ResultSet res = createStmt.getGeneratedKeys();
            res.next();
            return get(res.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Server get(Integer key) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from Server u where idServer = ? LIMIT 1");
            pstmt.setInt(1, key);
            ResultSet resServer = pstmt.executeQuery();

            if (!resServer.next()) {
                return null;
            }

            Server s = new Server(resServer.getString("name"));
            s.setKey(resServer.getInt("idServer"));

            s.getManyToManyReferences().put("users", new ArrayList<>());

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * from User JOIN Server_has_User ShU on User.pseudo = ShU.User_pseudo JOIN Server S on S.idServer = ShU.Server_idServer where idServer = ?");
            stmt.setInt(1, resServer.getInt("idServer"));
            ResultSet resUser = stmt.executeQuery();

            while (resUser.next()) {
                s.getManyToManyReferences().get("users").add(resUser.getString("pseudo"));
            }

            return s;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean delete(Integer key) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Server WHERE idServer = ?");
            pstmt.setInt(1, key);
            pstmt.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Server update(Server object) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("UPDATE Server  SET name = ? WHERE idServer = ?");
            pstmt.setString(1, object.getName());
            pstmt.setInt(2, (Integer) object.getKey());
            pstmt.execute();

            return get((Integer) object.getKey());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<Server> list() {
        ArrayList<Server> servers = new ArrayList<>();

        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from Server u");
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                Server s = new Server(res.getString("name"));
                s.setKey(res.getInt("idServer"));
                s.getManyToManyReferences().put("users", new ArrayList<>());

                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * from User JOIN Server_has_User ShU on User.pseudo = ShU.User_pseudo JOIN Server S on S.idServer = ShU.Server_idServer where idServer = ?");
                stmt.setInt(1, res.getInt("idServer"));
                ResultSet resUser = stmt.executeQuery();

                while (resUser.next()) {
                    s.getManyToManyReferences().get("users").add(resUser.getString("pseudo"));
                }

                servers.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return servers;
    }
}
