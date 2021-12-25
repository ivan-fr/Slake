package sockets;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ClientServerRunner implements Runnable {

    public static List<ClientServerRunner> runningServers = new ArrayList<>() ;
    private Socket clientSocket ;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;

    public ClientServerRunner(Socket clientSocket) throws IOException {
        try {
            this.clientSocket = clientSocket;
            this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())) ;
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ;
            this.username = reader.readLine();
            runningServers.add(this) ;
            broadcastMessage("Server: " + username + " has joined.") ;
        }catch(IOException e) {
            closeEverything(clientSocket, reader, writer) ;
        }

    }


    @Override
    public void run() {
        String message;

        while (clientSocket.isConnected()) {
            try {
                message = reader.readLine();
                broadcastMessage(message);
            } catch (IOException e) {
                closeEverything(clientSocket, reader, writer) ;
                break;
            }
        }
    }

    private void broadcastMessage(String message) {
        for (ClientServerRunner runningServer : runningServers) {
            try {
                if(!runningServer.username.equals(this.username)) {
                    runningServer.writer.write(message);
                    runningServer.writer.newLine();
                    runningServer.writer.flush();
                }
            } catch (IOException e) {
                closeEverything(clientSocket, reader, writer) ;
            }
        }
    }

    private void closeEverything(Socket clientSocket, BufferedReader reader, BufferedWriter writer) {
        removeServer();
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

    private void removeServer() {
        runningServers.remove(this) ;
        broadcastMessage("SERVER " + username + " has left");
    }



}
