package sockets;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class ClientHandler {

    private final Socket clientSocket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private boolean menu_mode = true;
    private Thread threadMessage = null;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void run() {
        while (clientSocket.isConnected()) {
            int choose;

            if (!menu_mode) {
                try {
                    actionDispatcher(8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("""
                        Choose a action:
                        1 - connection
                        2 - join a server
                        3 - join a channel (required to join a server before)
                        4 - show servers
                        5 - show channels (required to join a server before)
                        6 - disconnect from server
                        10 - create Server
                        11 - create Channel (required to join a server before)
                        12 - delete Server
                        13 - delete Channel
                        """);
                Scanner scanner = new Scanner(System.in);
                choose = scanner.nextInt();
                try {
                    actionDispatcher(choose);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void connection() throws IOException {
        boolean first = true;
        do {
            if (!first) {
                System.out.println("Wrong username");
            }

            System.out.println("""
                    Give a username
                    """);

            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();

            writer.write(1);
            writer.write(username);
            writer.newLine();
            writer.flush();

            first = false;
        } while (reader.read() == 0);

        System.out.println("You are connected");
    }

    public void joinServer() throws IOException {
        boolean first = true;
        do {
            if (!first) {
                System.out.println("Wrong server id");
            }

            System.out.println("""
                    Give a server id
                    """);
            Scanner scanner = new Scanner(System.in);
            int serverId = scanner.nextInt();
            writer.write(2);
            writer.write(serverId);
            writer.flush();
            first = false;
        } while (reader.read() == 0);

        System.out.println("You are in server");
    }

    public void joinChannel() throws IOException {
        menu_mode = false;

        boolean first = true;
        do {
            if (!first) {
                System.out.println("Wrong channel id");
            }

            System.out.println("""
                    Give a channel id
                    """);
            Scanner scanner = new Scanner(System.in);
            int channel_id = scanner.nextInt();
            writer.write(3);
            writer.write(channel_id);
            writer.flush();
            first = false;
        } while (reader.read() == 0);

        System.out.println("You are in channel");

        threadMessage = new Thread(() -> {
            while (clientSocket.isConnected()) {
                try {
                    int choose = reader.read();

                    if (choose == 7) {
                        actionDispatcher(choose);
                    } else {
                        System.out.println("Error: the server shouldn't send this action.." + choose);
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        threadMessage.start();
    }

    public void showServers() throws IOException {
        writer.write(4);
        writer.flush();
        String response;
        while (!Objects.equals(response = reader.readLine(), "")) {
            System.out.println(response);
        }
        System.out.println("OK.");
    }

    public void showChannels() throws IOException {
        writer.write(5);
        writer.flush();
        String response;
        while (!Objects.equals(response = reader.readLine(), "")) {
            System.out.println(response);
        }
        System.out.println("OK.");
    }

    public void actionDispatcher(Integer action) throws IOException {
        switch (action) {
            case 1 -> connection();
            case 2 -> joinServer();
            case 3 -> joinChannel();
            case 4 -> showServers();
            case 5 -> showChannels();
            case 6 -> closeEverything();
            case 7 -> receiveMessage();
            case 8 -> sendMessage();
            case 10 -> createServer();
            case 11 -> createChannel();
            case 12 -> deleteServer();
            case 13 -> deleteChannel();
        }
    }

    public void deleteChannel() throws IOException {
        boolean first = true;
        do {
            if (!first) {
                System.out.println("problem in creation...");
            }

            System.out.println("""
                    Give a channel id
                    """);
            Scanner scanner = new Scanner(System.in);
            int serverId = scanner.nextInt();
            writer.write(13);
            writer.write(serverId);
            writer.flush();
            first = false;
        } while (reader.read() == 0);

        System.out.println("Channel deleted");
    }

    public void deleteServer() throws IOException {
        boolean first = true;
        do {
            if (!first) {
                System.out.println("problem in creation...");
            }

            System.out.println("""
                    Give a server id
                    """);
            Scanner scanner = new Scanner(System.in);
            int serverId = scanner.nextInt();
            writer.write(12);
            writer.write(serverId);
            writer.flush();
            first = false;
        } while (reader.read() == 0);

        System.out.println("Server deleted");
    }

    public void createChannel() throws IOException {
        boolean first = true;
        do {
            if (!first) {
                System.out.println("problem in creation...");
            }

            System.out.println("""
                    Give a channel name
                    """);
            Scanner scanner = new Scanner(System.in);
            String serverName = scanner.nextLine();
            writer.write(11);
            writer.write(serverName);
            writer.newLine();
            writer.flush();
            first = false;
        } while (reader.read() == 0);

        System.out.println("Channel created");
    }

    public void createServer() throws IOException {
        boolean first = true;
        do {
            if (!first) {
                System.out.println("problem in creation...");
            }

            System.out.println("""
                    Give a server name
                    """);
            Scanner scanner = new Scanner(System.in);
            String serverName = scanner.nextLine();
            writer.write(10);
            writer.write(serverName);
            writer.newLine();
            writer.flush();
            first = false;
        } while (reader.read() == 0);

        System.out.println("Server created");
    }

    public void sendMessage() throws IOException {
        if (menu_mode) {
            return;
        }

        System.out.println("Enter a message: ");
        Scanner scanner = new Scanner(System.in);
        String msgContent = scanner.nextLine();

        if (msgContent.contentEquals("quit")) {
            writer.write(9);
            writer.flush();
            threadMessage.interrupt();
            threadMessage = null;
            menu_mode = true;
        } else {
            writer.write(7);
            writer.flush();
            writer.write(msgContent);
            writer.newLine();
            writer.flush();
        }
    }

    public void receiveMessage() throws IOException {
        System.out.println("read message...");
        String response;
        while (!Objects.equals(response = reader.readLine(), "")) {
            System.out.println(response);
        }
    }

    private void closeEverything() throws IOException {
        if (threadMessage != null) {
            threadMessage.interrupt();
            threadMessage = null;
        }

        writer.write(6);
        writer.flush();
        writer.close();

        if (reader != null) {
            reader.close();
        }
        if (clientSocket != null) {
            clientSocket.close();
        }
    }
}
