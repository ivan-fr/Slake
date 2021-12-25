package sockets;


import models.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket clientSocket ;
    private BufferedReader reader;
    private BufferedWriter writer;
    private User user;

    public Client(Socket clientSocket, User user) {
        try {
            this.clientSocket = clientSocket;
            this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())) ;
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ;
            this.user = user;
        }catch(IOException e) {
            closeEverything(clientSocket, reader, writer) ;
        }
    }

    private void closeEverything(Socket clientSocket, BufferedReader reader, BufferedWriter writer) {
        try {
            if(writer!=null) {
                writer.close();
            }
            if (reader!=null) {
                reader.close();
            }
            if (clientSocket!=null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessages() {
        try {
            // sending username
            writer.write(user.getPseudo());
            writer.newLine();
            writer.flush();

            // sending currentServer
            writer.write(user.getCurrentServer());
            writer.newLine();
            writer.flush();

            // sending currentChannel
            writer.write(user.getCurrentChannel());
            writer.newLine();
            writer.flush();

            Scanner scanner = new Scanner(System.in);
            while (clientSocket.isConnected()) {
                String message = scanner.nextLine();
                if (!message.isEmpty() && !message.isBlank()){
                    writer.write(user.getPseudo() + ": " + message);
                    writer.newLine();
                    writer.flush();
                }
            }

        } catch (IOException e) {
            closeEverything(clientSocket, reader, writer) ;
        }
    }

    public void receiveMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;

                while (clientSocket.isConnected()) {
                    try {
                        message = reader.readLine();
                        System.out.println(message);
                    } catch (IOException e) {
                        closeEverything(clientSocket, reader, writer) ;
                    }
                }
            }
        }).start();
    }


}
