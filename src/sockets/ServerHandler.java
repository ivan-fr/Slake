package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler {

    private final ServerSocket serverSocket;
    private final static int PORT = 1255 ;

    public ServerHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        try {

            while(!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("A new client has joined");
                ClientServerRunner clientServerRunner = new ClientServerRunner(clientSocket);
                Thread thread = new Thread(clientServerRunner);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket!=null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
