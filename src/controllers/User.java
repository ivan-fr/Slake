package controllers;

import bdd.SingletonConnection;
import models.Server;

import java.sql.*;
import java.util.ArrayList;

public class User {
    public static boolean createUser(String pseudo) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO User (pseudo) VALUES (?)");
            pstmt.setString(1, pseudo);
            return pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static models.User getUser(String pseudo) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from User u where pseudo = ?");
            pstmt.setString(1, pseudo);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) {
                return null;
            }

            return new models.User(res.getInt("idUser"), res.getString("pseudo"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getUserServer(models.User user) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from Server c join User_has_Server UhS on c.idServer = UhS.Server_idServer join User U on U.idUser = UhS.User_idUser where U.idUser = ?");
            pstmt.setInt(1, user.getUserId());
            ResultSet res_servers = pstmt.executeQuery();

            ArrayList<Server> user_servers = user.getsServers();
            Server server;
            while (res_servers.next()) {
                server = new Server(res_servers.getInt("idServer"), res_servers.getString("name"));
                user_servers.add(server);
//                server.getUsers().add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
