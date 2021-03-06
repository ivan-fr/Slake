package repository;

import bdd.SingletonConnection;
import models.Channel;

import java.sql.*;
import java.util.ArrayList;

public class ChannelRepository implements IRepository<Channel, Integer> {
    public final static ChannelRepository channelRepository = new ChannelRepository();

    @Override
    public Channel save(Channel object) {
        Connection conn = SingletonConnection.connection;

        try {
            assert conn != null;
            PreparedStatement createStmt = conn.prepareStatement(
                    "INSERT INTO Channel (Server_idServer, name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            createStmt.setInt(1, (Integer) object.getServer().getKey());
            createStmt.setString(2, object.getName());
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
    public Channel get(Integer key) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from Channel u where idChannel = ? LIMIT 1");
            pstmt.setInt(1, key);
            ResultSet res = pstmt.executeQuery();

            if (!res.next()) {
                return null;
            }

            Channel c = new Channel(res.getString("name"), res.getInt("Server_idServer"));
            c.getManyToOneReferences().put("server", res.getInt("Server_idServer"));
            c.setKey(res.getInt("idChannel"));

            PreparedStatement stmtMessages = conn.prepareStatement("SELECT * from Message m join Channel C on C.idChannel = m.Channel_idChannel where idChannel = ?");
            stmtMessages.setInt(1, key);
            ResultSet resMessages = stmtMessages.executeQuery();

            c.getOneToManyReferences().put("messages", new ArrayList<>());

            while (resMessages.next()) {
                c.getOneToManyReferences().get("messages").add(resMessages.getInt("idMessage"));
            }

            return c;
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
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Channel WHERE idChannel = ?");
            stmt.setInt(1, key);
            stmt.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Channel update(Channel object) {
        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("UPDATE Channel  SET name = ? WHERE idChannel = ?");
            stmt.setString(1, object.getName());
            stmt.setInt(2, (Integer) object.getKey());
            stmt.execute();

            return get((Integer) object.getKey());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<Channel> list() {
        ArrayList<Channel> channels = new ArrayList<>();

        Connection conn = SingletonConnection.connection;
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from Channel u");
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                Channel c = new Channel(res.getString("name"), res.getInt("Server_idServer"));
                c.getManyToOneReferences().put("server", res.getInt("Server_idServer"));
                c.setKey(res.getInt("idChannel"));
                channels.add(c);

                c.getOneToManyReferences().put("messages", new ArrayList<>());

                PreparedStatement stmtMessages = conn.prepareStatement("SELECT * from Message m join Channel C on C.idChannel = m.Channel_idChannel where idChannel = ?");
                stmtMessages.setInt(1, res.getInt("idChannel"));
                ResultSet resMessages = stmtMessages.executeQuery();

                while (resMessages.next()) {
                    c.getOneToManyReferences().get("messages").add(resMessages.getInt("idMessage"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return channels;
    }
}
