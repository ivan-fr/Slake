package sockets;


import composite.CompositeChannelSingleton;
import composite.CompositeMessageSingleton;
import composite.CompositeServerSingleton;
import composite.CompositeUserSingleton;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    private final static int PORT = 1255 ;

    public static void main(String[] args) throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Mysql implementation is OK.");
        } catch (Exception ex) {
            System.out.println("Mysql implementation error.");
        }

        CompositeUserSingleton.compositeUserSingleton.hydrate();
        CompositeServerSingleton.compositeServerSingleton.hydrate();
        CompositeChannelSingleton.compositeChannelSingleton.hydrate();
        CompositeMessageSingleton.compositeMessageSingleton.hydrate();

        ServerSocket serverSocket = new ServerSocket(PORT);
        ServerHandler serverHandler = new ServerHandler(serverSocket);
        System.out.println("Server started.");
        serverHandler.start();
    }
}
