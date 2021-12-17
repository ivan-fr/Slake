package main;

import composite.CompositeChannelSingleton;
import composite.CompositeMessageSingleton;
import composite.CompositeServerSingleton;
import composite.CompositeUserSingleton;
import models.User;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Mysql implementation is OK.");

            // System.out.println(u);
        } catch (Exception ex) {
            System.out.println("Mysql implementation error.");
        }


        CompositeUserSingleton.compositeUserSingleton.hydrate();
        CompositeServerSingleton.compositeServerSingleton.hydrate();
        CompositeChannelSingleton.compositeChannelSingleton.hydrate();
        CompositeMessageSingleton.compositeMessageSingleton.hydrate();

        List<User> users = CompositeUserSingleton.compositeUserSingleton.list();

        for (User user : users) {
            System.out.println(user.toString());
        }

    }
}
