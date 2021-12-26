package sockets;

import java.io.*;
import java.net.Socket;
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
        new Thread(() -> {
            while (clientSocket.isConnected()) {
                int choose;

                if (!menu_mode) {
                    choose = 8;
                } else {
                    System.out.println("""
                            Choose a action:
                            1 - connection
                            2 - join a server
                            3 - join a channel (required to join a server before)
                            4 - show servers
                            5 - show channels (required to join a server before)
                            6 - disconnect
                            """);
                    Scanner scanner = new Scanner(System.in);
                    choose = scanner.nextInt();
                }

                try {
                    actionDispatcher(choose);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
            writer.newLine();
            writer.flush();
            first = false;
        } while (reader.read() == 0);

        System.out.println("You are in server");
    }

    public void joinChannel() throws IOException {
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
            writer.newLine();
            writer.flush();
            first = false;
        } while (reader.read() == 0);

        System.out.println("You are in channel");

        menu_mode = false;

        threadMessage = new Thread(() -> {
            while (clientSocket.isConnected()) {
                if (!menu_mode) {
                    try {
                        actionDispatcher(reader.read());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadMessage.start();
    }

    public void showServers() throws IOException {
        writer.write(4);
        writer.flush();
        String response;
        while ((response = reader.readLine()) != null){
            System.out.println(response);
        }
    }

    public void showChannels() throws IOException {
        writer.write(5);
        writer.flush();
        String response;
        while ((response = reader.readLine()) != null){
            System.out.println(response);
        }
    }

    public synchronized void actionDispatcher(Integer action) throws IOException {
        switch (action) {
            case 1 -> connection();
            case 2 -> joinServer();
            case 3 -> joinChannel();
            case 4 -> showServers();
            case 5 -> showChannels();
            case 6 -> closeEverything();
            case 7 -> receiveMessage();
            case 8 -> sendMessage();
        }
    }

    public void sendMessage() throws IOException {
        if (menu_mode) {
            return;
        }

        System.out.println("Enter a message: ");
        Scanner scanner = new Scanner(System.in);
        String msgContent = scanner.nextLine();

        if (msgContent.contentEquals("quit"))
        {
            writer.write(9);
            writer.flush();
            threadMessage.interrupt();
            menu_mode = true;
        } else {
            writer.write(7);
            writer.write(msgContent);
            writer.newLine();
            writer.flush();
        }
    }

    public void receiveMessage() throws IOException {
        String response;
        while ((response = reader.readLine()) != null){
            System.out.println(response);
        }
    }

    private void closeEverything() throws IOException {
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
