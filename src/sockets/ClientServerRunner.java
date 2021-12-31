package sockets;

import composite.CompositeChannelSingleton;
import composite.CompositeMessageSingleton;
import composite.CompositeServerSingleton;
import composite.CompositeUserSingleton;
import models.Channel;
import models.Message;
import models.Server;
import models.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class ClientServerRunner implements Runnable {
    public static List<ClientServerRunner> runningServers = new ArrayList<>();
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private String me;
    private Integer selectedServer;
    private Integer selectedChannel;

    public ClientServerRunner(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        while (clientSocket.isConnected()) {
            try {
                Integer action = reader.read();
                actionDispatcher(action);
            } catch (IOException e) {
                try {
                    closeEverything(clientSocket, reader, writer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }

    public void actionDispatcher(Integer action) throws IOException {
        switch (action) {
            case 1 -> connection();
            case 2 -> joinServer();
            case 3 -> joinChannel();
            case 4 -> showServers();
            case 5 -> showChannels();
            case 6 -> closeEverything(clientSocket, reader, writer);
            case 7 -> writeMessage();
            case 9 -> quitChannel();
        }
    }

    public void quitChannel() {
        selectedChannel = null;
    }

    public void writeMessage() throws IOException {
        Message msg = new Message(reader.readLine(), new Date(), CompositeUserSingleton.compositeUserSingleton.get(me).getPseudo(), selectedChannel);
        msg = CompositeMessageSingleton.compositeMessageSingleton.save(msg);
        broadcastMessage(msg);
    }

    public void showChannels() throws IOException {
        writer.write(String.valueOf(selectedServer));
        writer.newLine();
        writer.write("");
        writer.flush();
    }

    public void showServers() throws IOException {
        writer.write(
                String.format(
                        "%s", CompositeServerSingleton.compositeServerSingleton.list().stream()
                                .map(Server::toString)
                                .collect(Collectors.joining("", "", ""))
                )
        );
        writer.newLine();
        writer.write("");
        writer.flush();
    }

    public void joinServer() throws IOException {
        if (me == null) {
            System.out.println("me is null");
            this.writer.write(-1);
            this.writer.flush();
            return;
        }

        selectedServer = null;
        selectedChannel = null;

        Integer serverID = reader.read();
        Server s = CompositeServerSingleton.compositeServerSingleton.get(serverID);

        if (s == null) {
            System.out.println("server channel not exist");
            this.writer.write(0);
            this.writer.flush();
            return;
        }

        User u = CompositeUserSingleton.compositeUserSingleton.addServer(CompositeUserSingleton.compositeUserSingleton.get(me), s);
        System.out.println(u.toStringWithoutRelation() + " join server " + s.toStringWithoutRelation());

        selectedServer = serverID;
        this.writer.write(1);
        this.writer.flush();
    }

    public void joinChannel() throws IOException {
        if (me == null) {
            this.writer.write(-1);
            this.writer.flush();
        }

        if (selectedServer == null) {
            this.writer.write(-1);
            this.writer.flush();
        }

        selectedChannel = null;
        Integer channelID = reader.read();

        Channel c = CompositeChannelSingleton.compositeChannelSingleton.get(channelID);

        if (c == null) {
            this.writer.write(0);
            this.writer.flush();
        } else {
            selectedChannel = channelID;
            this.writer.write(1);
            this.writer.flush();
        }
    }

    public void connection() throws IOException {
        System.out.println("connection process");
        String username = reader.readLine();
        System.out.println("=======" + username + "======");
        User userExist = CompositeUserSingleton.compositeUserSingleton.get(username);

        if (userExist == null) {
            this.writer.write(0);
            this.writer.flush();
            return;
        }

        me = (String) userExist.getKey();
        runningServers.add(this);

        this.writer.write(1);
        this.writer.flush();

        System.out.println(new Date() + " : " + userExist.getPseudo() + " joined.");
    }

    private void broadcastMessage(Message message) throws IOException {
        System.out.println("broadcast message process");
        if (this.selectedServer == null) {
            System.out.println("broadcast message process selected server is null");
            return;
        }

        if (this.selectedChannel == null) {
            System.out.println("broadcast message process selected channel is null");
            return;
        }

        for (ClientServerRunner runningServer : runningServers) {
            if (runningServer.selectedServer == null) {
                continue;
            }

            if (runningServer.selectedChannel == null) {
                continue;
            }

            try {
                if (runningServer.selectedServer.equals(this.selectedServer)
                        && runningServer.selectedChannel.equals(this.selectedChannel)) {
                    System.out.println("go broadcast");

                    runningServer.writer.write(7);
                    runningServer.writer.write(String.valueOf(message.toStringWithoutRelation()));
                    runningServer.writer.newLine();
                    runningServer.writer.write("");
                    runningServer.writer.flush();
                }
            } catch (IOException ex) {
                closeEverything(runningServer.clientSocket, runningServer.reader, runningServer.writer);
            }
        }
    }

    private void closeEverything(Socket clientSocket, BufferedReader reader, BufferedWriter writer) throws IOException {
        removeServer();
        if (writer != null) {
            writer.close();
        }
        if (reader != null) {
            reader.close();
        }
        if (clientSocket != null) {
            clientSocket.close();
        }
    }

    private void removeServer() {
        runningServers.remove(this);
    }
}
