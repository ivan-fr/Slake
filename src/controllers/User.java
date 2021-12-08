package controllers;

import bdd.SingletonConnection;
import models.Message;
import models.Room;

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
            PreparedStatement pstmt = conn.prepareStatement("SELECT u.pseudo from User u where pseudo = ?");
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

    public static void getUserMessages(models.User user) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from Message m where m.user_source = ?");
            pstmt.setInt(1, user.getUserId());
            ResultSet res_msg = pstmt.executeQuery();

            ArrayList<Message> msgs = user.getMessages();
            while (res_msg.next()) {
                PreparedStatement pstmt_room = conn.prepareStatement("SELECT * from Room r where r.idRoom = ?");
                pstmt.setInt(1, res_msg.getInt("Room_idRoom"));
                ResultSet res_room = pstmt_room.executeQuery();
                Room room = null;

                if (res_room.next()) {
                    room = new Room(res_room.getInt("idRoom"), res_room.getString("name"), res_room.getInt("userCounter"));
                }

                PreparedStatement pstmt_user_dest = conn.prepareStatement("SELECT * from User u where u.idUser = ?");
                pstmt.setInt(1, res_msg.getInt("user_destination"));
                ResultSet res_user_dest = pstmt_user_dest.executeQuery();

                if(!res_user_dest.next()) {
                    continue;
                }

                models.User user_dest = new models.User(res_user_dest.getInt("idUser"), res_user_dest.getString("pseudo"));

                if (res_room.next()) {
                    room = new Room(res_room.getInt("idRoom"), res_room.getString("name"), res_room.getInt("userCounter"));
                }

                msgs.add(new Message(res_msg.getString("content"), res_msg.getInt("Room_idRoom"), room, res_msg.getInt("user_source"), res_msg.getInt("user_destination"), user, user_dest));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
