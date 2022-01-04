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
import java.util.Objects;
import java.util.stream.Collectors;


public class ClientServerRunner implements Runnable {
    public static List<ClientServerRunner> runningServers = new ArrayList<>();
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private String me = null;
    private Integer selectedServer = null;
    private Integer selectedChannel = null;

    public ClientServerRunner(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        while (clientSocket.isConnected()) {
            try {
                int action = reader.read();
                if (action == -1) {
                    break;
                }
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
        System.out.println("receive action : " + action);
        switch (action) {
            case 1 -> connection();
            case 2 -> joinServer();
            case 3 -> joinChannel();
            case 4 -> showServers();
            case 5 -> showChannels();
            case 6 -> closeEverything(clientSocket, reader, writer);
            case 7 -> writeMessage();
            case 9 -> quitChannel();
            case 10 -> createServer();
            case 11 -> createChannel();
            case 12 -> deleteServer();
            case 13 -> deleteChannel();
            case 14 -> cancel();
            case 15 -> createUser();
            case 16 -> showMessages();
            case 17 -> writeJoinChannel();
        }
    }

    public void cancel() throws IOException {
        this.writer.write(0);
        this.writer.flush();
    }

    public void deleteServer() throws IOException {
        Integer serverId = this.reader.read();
        CompositeServerSingleton.compositeServerSingleton.delete(serverId);

        CompositeUserSingleton.compositeUserSingleton.hydrate();
        CompositeServerSingleton.compositeServerSingleton.hydrate();
        CompositeChannelSingleton.compositeChannelSingleton.hydrate();
        CompositeMessageSingleton.compositeMessageSingleton.hydrate();

        selectedServer = null;
        selectedChannel = null;

        this.writer.write(1);
        this.writer.flush();
    }

    public void deleteChannel() throws IOException {
        Integer channelId = this.reader.read();
        CompositeChannelSingleton.compositeChannelSingleton.delete(channelId);

        CompositeUserSingleton.compositeUserSingleton.hydrate();
        CompositeServerSingleton.compositeServerSingleton.hydrate();
        CompositeChannelSingleton.compositeChannelSingleton.hydrate();
        CompositeMessageSingleton.compositeMessageSingleton.hydrate();

        selectedChannel = null;

        this.writer.write(1);
        this.writer.flush();
    }

    public void createServer() throws IOException {
        String serverName = this.reader.readLine();
        Server s = new Server(serverName);
        CompositeServerSingleton.compositeServerSingleton.save(s);
        this.writer.write(1);
        this.writer.flush();
    }

    public void createUser() throws IOException {
        String pseudo = this.reader.readLine();
        User u = new User(pseudo);

        if (CompositeUserSingleton.compositeUserSingleton.save(u) == null)
        {
            this.writer.write(0);
            this.writer.flush();
            return;
        }

        this.writer.write(1);
        this.writer.flush();
    }

    public void createChannel() throws IOException {
        String channelName = this.reader.readLine();

        if (selectedServer == null) {
            System.out.println(selectedServer);
            this.writer.write(0);
            this.writer.flush();
        }

        Channel c = new Channel(channelName, selectedServer);
        CompositeChannelSingleton.compositeChannelSingleton.save(c);
        CompositeServerSingleton.compositeServerSingleton.hydrate();

        this.writer.write(1);
        this.writer.flush();
    }

    public void quitChannel() throws IOException {
        broadcastMessage(new Message("<SERVER> Has left", new Date(), CompositeUserSingleton.compositeUserSingleton.get(me).getPseudo(), selectedChannel));
        selectedChannel = null;
        this.writer.write(100);
        this.writer.flush();
    }

    public void writeJoinChannel() throws IOException {
        broadcastMessage(new Message("<SERVER> Has joined", new Date(), CompositeUserSingleton.compositeUserSingleton.get(me).getPseudo(), selectedChannel));
    }

    public void writeMessage() throws IOException {
        Message msg = new Message(reader.readLine(), new Date(), CompositeUserSingleton.compositeUserSingleton.get(me).getPseudo(), selectedChannel);
        msg = CompositeMessageSingleton.compositeMessageSingleton.save(msg);
        broadcastMessage(msg);
    }

    public void showChannels() throws IOException {
        if (selectedServer == null) {
            return;
        }
        writer.write(String.valueOf(CompositeServerSingleton.compositeServerSingleton.get(selectedServer)));
        writer.newLine();
        writer.write("0");
        writer.newLine();
        writer.flush();
    }

    public void showServers() throws IOException {
        writer.write(
            String.format(
                "%s", CompositeServerSingleton.compositeServerSingleton.list().stream()
                        .map(Server::toString)
                        .collect(Collectors.joining(" ", " ", " "))
            )
        );
        writer.newLine();
        writer.write("0");
        writer.newLine();
        writer.flush();
    }

    public void showMessages() throws IOException {
        if (selectedServer == null || selectedChannel == null) {
            return;
        }

        writer.write(
                String.format(
                        "%s", CompositeChannelSingleton.compositeChannelSingleton.get(selectedChannel).getMessages().stream()
                                .map(Message::toStringWithoutRelation)
                                .collect(Collectors.joining(" ", " ", " "))
                )
        );
        writer.newLine();
        writer.write("0");
        writer.newLine();
        writer.flush();
    }

    public void joinServer() throws IOException {
        if (me == null) {
            System.out.println("me is null");
            this.writer.write(0);
            this.writer.flush();
            return;
        }

        this.writer.write(1);
        this.writer.flush();

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
            this.writer.write(0);
            this.writer.flush();
            System.out.println("send 0");
            return;
        }

        if (selectedServer == null) {
            this.writer.write(0);
            this.writer.flush();
            System.out.println("send 0");
            return;
        }

        this.writer.write(1);
        this.writer.flush();

        selectedChannel = null;
        Integer channelID = reader.read();

        Channel c = CompositeChannelSingleton.compositeChannelSingleton.get(channelID);

        if (c == null) {
            System.out.println("send 0");
            this.writer.write(0);
            this.writer.flush();
        } else {
            System.out.println("user join channel");
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
                        && runningServer.selectedChannel.equals(this.selectedChannel) &&
                        !Objects.equals(runningServer.me, me) && message.getContent().trim().length() > 0
                ) {
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
