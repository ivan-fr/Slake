package sockets;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    private final static int PORT = 1255 ;


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Server server = new Server(serverSocket);
        System.out.println("Server started.");
        server.start();
    }
}
