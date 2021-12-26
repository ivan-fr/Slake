package sockets;

import composite.CompositeChannelSingleton;
import composite.CompositeMessageSingleton;
import composite.CompositeServerSingleton;
import composite.CompositeUserSingleton;
import models.Server;
import models.User;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientMain {

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

        List<User> users = CompositeUserSingleton.compositeUserSingleton.list();

        Scanner scanner = new Scanner(System.in) ;


        //getting user
        boolean userExist = false;
        User connectedUser = null;
        String username = "" ;
        while(!userExist) {
            System.out.println("Enter username: ");
            username = scanner.nextLine();
            for (User user : users) {
                if (username.equals(user.getPseudo())) {
                    userExist = true;
                    connectedUser = user;
                    break;
                }
            }
            if (!userExist) System.out.println("user not found in system! Try again");
        }

        //getting server
        connectedUser.showServers();
        Server server;
        String serverName;

       do {
           System.out.println("Choose a server: ");
           serverName = scanner.nextLine();

       }while ((server=connectedUser.getServer(serverName)) == null);


        // getting channel
        server.showChannels();
        String channelName;
        do {
            System.out.println("Choose a channel from " + serverName +" : ");
            channelName = scanner.nextLine();

        }while (!server.gotChannel(channelName));

        connectedUser.setCurrentServer(serverName);
        connectedUser.setCurrentChannel(channelName);

        System.out.println("You are connected as " + username + " to " +connectedUser.getCurrentServer() + "-" + connectedUser.getCurrentChannel());

        Socket socket = new Socket("localhost", 1255);

        ClientHandler clientHandler = new ClientHandler(socket, connectedUser);

        clientHandler.receiveMessages();
        clientHandler.sendMessages();
    }
}
