package sockets;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1255);
        ClientHandler clientHandler = new ClientHandler(socket);
        clientHandler.run();
    }
}
