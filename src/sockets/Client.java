package sockets;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket clientSocket ;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;

    public Client(Socket clientSocket, String username) {
        try {
            this.clientSocket = clientSocket;
            this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())) ;
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ;
            this.username = username;
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
            writer.write(username);
            writer.newLine();
            writer.flush();

            Scanner scanner = new Scanner(System.in);
            while (clientSocket.isConnected()) {
                String message = scanner.nextLine();
                if (!message.isEmpty() && !message.isBlank()){
                    writer.write(username + ": " + message);
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
