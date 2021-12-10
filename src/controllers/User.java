package controllers;

import bdd.SingletonConnection;
import models.Server;
import system.SlakeSystem;

import java.sql.*;

public class User {
    public static models.User createUser(String pseudo) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement createStmt = conn.prepareStatement("INSERT INTO User (pseudo) VALUES (?)");
            createStmt.setString(1, pseudo);
            return getUserFromBDD(pseudo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static models.User getUserFromBDD(String pseudo) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from User u where pseudo = ? LIMIT 1");
            pstmt.setString(1, pseudo);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) {
                return null;
            }

            SlakeSystem ss = SlakeSystem.slakeSystem;
            if (ss.getUserMap().containsKey(res.getInt("idUser"))) {
                return ss.getUserMap().get(res.getInt("idUser"));
            }
            models.User u = new models.User(res.getInt("idUser"), res.getString("pseudo"));
            ss.getUserMap().put(res.getInt("idUser"), u);
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getUserServersFromBDD(models.User user) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from Server c join User_has_Server UhS on c.idServer = UhS.Server_idServer join User U on U.idUser = UhS.User_idUser where U.idUser = ?");
            pstmt.setInt(1, user.getUserId());
            ResultSet res_servers = pstmt.executeQuery();

            user.getsServers().clear();

            SlakeSystem ss = SlakeSystem.slakeSystem;

            Server server;
            while (res_servers.next()) {
                if (ss.getServerMap().containsKey(res_servers.getInt("idServer"))) {
                    server = ss.getServerMap().get(user.getUserId());
                } else {
                    server = new Server(res_servers.getInt("idServer"), res_servers.getString("name"));
                    ss.getServerMap().put(user.getUserId(), server);
                }
                server.getUsers().add(user);
                user.getsServers().add(server);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
