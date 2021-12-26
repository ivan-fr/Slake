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
    private User me;
    private Server selectedServer;
    private Channel selectedChannel;

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
        Message msg = new Message(reader.readLine(), new Date(), me.getPseudo(), (Integer) selectedChannel.getKey());
        msg = CompositeMessageSingleton.compositeMessageSingleton.save(msg);
        broadcastMessage(msg);
    }

    public void showChannels() throws IOException {
        writer.write(String.valueOf(selectedServer));
        writer.flush();
    }

    public void showServers() throws IOException {
        writer.write(
                String.format("%s", CompositeServerSingleton.compositeServerSingleton.list().stream()
                        .map(Server::toString)
                        .collect(Collectors.joining("", "", ""))
                )
        );
        writer.flush();
    }

    public void joinServer() throws IOException {
        selectedServer = null;
        selectedChannel = null;

        Integer serverID = reader.read();

        selectedServer = CompositeServerSingleton.compositeServerSingleton.get(serverID);
        me = CompositeUserSingleton.compositeUserSingleton.addServer(me, selectedServer);

        if (selectedChannel == null) {
            this.writer.write(0);
            this.writer.flush();
        } else {
            this.writer.write(1);
            this.writer.flush();
        }
    }

    public void joinChannel() throws IOException {
        selectedChannel = null;
        Integer channelID = reader.read();

        selectedChannel = CompositeChannelSingleton.compositeChannelSingleton.get(channelID);

        if (selectedChannel == null) {
            this.writer.write(0);
            this.writer.flush();
        } else {
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

        me = userExist;
        runningServers.add(this);

        this.writer.write(1);
        this.writer.flush();

        System.out.println(new Date() + " : " + me.getPseudo() + " joined.");
    }

    private void broadcastMessage(Message message) throws IOException {
        if (this.selectedServer == null) {
            return;
        }

        if (this.selectedChannel == null) {
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
                if (!runningServer.me.getPseudo().equals(this.me.getPseudo())
                        && runningServer.selectedServer.equals(this.selectedServer)
                        && runningServer.selectedChannel.equals(this.selectedChannel)) {
                    runningServer.writer.write(7);
                    runningServer.writer.write(String.valueOf(message));
                    runningServer.writer.newLine();
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
