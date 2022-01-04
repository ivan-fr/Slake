package sockets;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class ClientHandler {
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private boolean connected = false;
    private boolean selectedServer = false;
    private boolean menu_mode = true;
    private Thread threadMessage = null;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void run() throws IOException {
        while (clientSocket.isConnected()) {
            int choose;

            /*
                Quand on rejoint un channel
                ça crée un thread qui permet d'écoute le serveur pour savoir si on reçois un message.
                On quitte aussi le mode menu pour passer en écriture de message (action 8).
             */
            if (!menu_mode) {
                try {
                    actionDispatcher(8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    System.out.println("""
                        ======================================================
                        Choose a action:
                        0 - create account
                        1 - connection
                        2 - join a server
                        3 - join a channel (required to join a server before)
                        4 - show servers
                        5 - show channels (required to join a server before)
                        6 - close socket
                        10 - create Server
                        11 - create Channel (required to join a server before)
                        12 - delete Server
                        13 - delete Channel
                        =======================================================
                        """);

                    System.out.print("Action : ");
                    Scanner scanner = new Scanner(System.in);
                    choose = scanner.nextInt();
                } catch (Exception e) {
                    continue;
                }

                try {
                    actionDispatcher(choose);
                } catch (IOException e) {
                    e.printStackTrace();
                    closeEverything();
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

            System.out.print("Give a username : ");

            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();

            writer.write(1);
            writer.write(username);
            writer.newLine();
            writer.flush();

            first = false;
        } while (reader.read() == 0);

        connected = true;
        System.out.println("You are connected");
    }

    public void joinServer() throws IOException {
        boolean first = true;
        boolean defer = true;
        do {
            if (!first) {
                System.out.println("Wrong server id");
            }

            System.out.println("Give a server id");
            Scanner scanner = new Scanner(System.in);

            int serverId = scanner.nextInt();

            if (serverId == -1) {
                defer = false;
                break;
            } else {
                writer.write(2);
                writer.flush();

                if (reader.read() == 0) {
                    first = false;
                    continue;
                }

                writer.write(serverId);
                writer.flush();

                if (reader.read() == 1) {
                    break;
                }
            }

            first = false;
        } while (true);

        if (defer) {
            selectedServer = true;
            System.out.println("You are in server");
        }
    }

    public void joinChannel() throws IOException {
        menu_mode = false;
        boolean defer = true;

        boolean first = true;
        do {
            if (!first) {
                System.out.println("Wrong channel id");
            }

            System.out.println("Give a channel id");
            Scanner scanner = new Scanner(System.in);
            int channel_id = scanner.nextInt();

            if (channel_id == -1) {
                defer = false;
                break;
            } else {
                writer.write(3);
                writer.flush();

                if (reader.read() == 0) {
                    first = false;
                    continue;
                };

                writer.write(channel_id);
                writer.flush();

                if (reader.read() == 1) {
                    break;
                }
            }
        } while (true);

        if (defer) {
            for (int i = 0; i < 30; i++) {
                System.out.println("");
            }

            System.out.println("You are in channel");
            System.out.println("Messages history");

            writer.write(16);
            writer.flush();

            while (true) {
                String rep = reader.readLine();
                try {
                    Integer test = Integer.parseInt(rep);
                    break;
                } catch (Exception e) {
                    System.out.println(rep);
                }
            }

            writer.write(17);
            writer.flush();

            /*
             Thread qui permet d'écouter la reception d'un message dans le channel que j'ai rejoint.
             */
            threadMessage = new Thread(() -> {
                while (clientSocket.isConnected()) {
                    try {
                        int choose = reader.read();

                        if (choose == 7) {
                            actionDispatcher(choose);
                        } else if (choose == 100) {
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadMessage.start();
        }
    }

    public void showServers() throws IOException {
        writer.write(4);
        writer.flush();
        while (true) {
            String rep = reader.readLine();
            try {
                Integer test = Integer.parseInt(rep);
                break;
            } catch (Exception e) {
                System.out.println(rep);
            }
        }
    }

    public void showChannels() throws IOException {
        if (!connected && !selectedServer) return;

        writer.write(5);
        writer.flush();
        while (true) {
            String rep = reader.readLine();
            try {
                Integer test = Integer.parseInt(rep);
                break;
            } catch (Exception e) {
                System.out.println(rep);
            }
        }
    }

    public void actionDispatcher(Integer action) throws IOException {
        switch (action) {
            case 0 -> createAccount();
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

    public void createAccount() throws IOException {
        boolean first = true;
        do {
            if (!first) {
                System.out.println("Pseudo already taken");
            }

            System.out.println("Give a pseudo");
            Scanner scanner = new Scanner(System.in);
            String pseudo = scanner.nextLine();
            writer.write(15);
            writer.write(pseudo);
            writer.newLine();
            writer.flush();
            first = false;
        } while (reader.read() == 0);

        System.out.println("User created");
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

        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        String msgContent = scanner.nextLine();

        if (msgContent.contentEquals("quit")) {
            menu_mode = true;
            threadMessage.interrupt();
            threadMessage = null;
            writer.write(9);
            writer.flush();
        } else {
            writer.write(7);
            writer.flush();
            writer.write(msgContent);
            writer.newLine();
            writer.flush();
        }
    }

    public void receiveMessage() throws IOException {
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
