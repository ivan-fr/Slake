package sockets;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket("localhost", 1255)) {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream() ;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String msg ;
            System.out.println("Im reading : ");
            while ((msg= br.readLine()) !=null) {
                outputStream.write(msg.getBytes("UTF-8"));
                System.out.println(msg);
            }
        }
    }
}
