package sockets;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

public class ServerRunner extends Thread {

    private final Socket clientSocket ;

    public ServerRunner(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)) ;
        String line ;

            StringWriter writer = new StringWriter();
        System.out.println("1");
            IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
        System.out.println("2");
            System.out.println("Client has sent : \n" + writer);
        System.out.println("client closed");
        clientSocket.close();
    }

}
