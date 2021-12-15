package repository;

import bdd.SingletonConnection;
import models.Message;

import java.sql.*;
import java.util.ArrayList;

public class MessageRepository implements IRepository<Message, Integer> {
    public final static MessageRepository messageRepository = new MessageRepository();

    @Override
    public Message save(Message object) {
        Connection conn = SingletonConnection.connection;

        try {
            assert conn != null;
            PreparedStatement createStmt = conn.prepareStatement("INSERT INTO Message (content, Channel_idChannel, date, User_pseudo) VALUES (?, ?, now(), ?)", Statement.RETURN_GENERATED_KEYS);
            createStmt.setString(1, object.getContent());
            createStmt.setInt(2, (Integer) object.getChannel().getKey());
            createStmt.setInt(3, (Integer) object.getUser().getKey());
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
    public Message get(Integer key) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from Message u where idMessage = ? LIMIT 1");
            pstmt.setInt(1, key);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) {
                return null;
            }

            Message m = new Message(res.getString("content"), res.getDate("date"));
            m.getManyToOneReferences().put("channel", res.getInt("Channel_idChannel"));

            PreparedStatement userStmt = conn.prepareStatement("SELECT * FROM User where pseudo = ?");
            userStmt.setInt(1, res.getInt("User_pseudo"));
            ResultSet userRes = userStmt.executeQuery();
            userRes.next();
            m.getManyToOneReferences().put("user", userRes.getString("pseudo"));

            m.setKey(res.getInt("idMessage"));
            return m;
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
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Message WHERE idMessage = ?");
            stmt.setInt(1, key);
            stmt.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Message update(Message object) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("UPDATE Message  SET content = ? WHERE idMessage = ?");
            pstmt.setString(1, object.getContent());
            pstmt.setInt(2, (Integer) object.getKey());
            pstmt.execute();

            return get((Integer) object.getKey());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<Message> list() {
        ArrayList<Message> messages = new ArrayList<>();

        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from Message u ORDER BY date");
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                Message m = new Message(res.getString("content"), res.getDate("date"));
                m.getManyToOneReferences().put("channel", res.getInt("Channel_idChannel"));

                PreparedStatement userStmt = conn.prepareStatement("SELECT * FROM User where pseudo = ?");
                userStmt.setInt(1, res.getInt("User_pseudo"));
                ResultSet userRes = userStmt.executeQuery();
                userRes.next();
                m.getManyToOneReferences().put("user", userRes.getString("pseudo"));

                m.setKey(res.getInt("idMessage"));
                messages.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
}
