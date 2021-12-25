package sockets;

import composite.CompositeChannelSingleton;
import composite.CompositeMessageSingleton;
import composite.CompositeServerSingleton;
import composite.CompositeUserSingleton;
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

        System.out.println("You are connected as " + username);
        System.out.println("Here are all you Servers\n" + connectedUser.getServers().toString());

        Socket socket = new Socket("localhost", 1255);

        Client client = new Client(socket, connectedUser.getPseudo());

        client.receiveMessages();
        client.sendMessages();
    }
}
