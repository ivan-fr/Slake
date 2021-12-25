package sockets;


import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SingletonMainServer {

    private final static int PORT = 1255 ;


    public static void startServerSocket() throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT) ;
            System.out.println("Waiting a connection...");
            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("A client is connected from " + clientSocket.getInetAddress().getHostAddress());
                ServerRunner serverRunner = new ServerRunner(clientSocket) ;
                serverRunner.run();
            }
        } catch (IOException e) {

        }

    }

    public static void main(String[] args) throws IOException {
        startServerSocket();
    }

}
