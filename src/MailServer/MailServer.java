package MailServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MailServer {
    public static void main(String[] args) throws IOException {
        ServerSocket mailServer = new ServerSocket(1);
        while (true) {
            Socket socket = null;
            try {
                socket = mailServer.accept();
                System.out.println("Client: " + socket);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                Thread thread = new ClientHandler(socket, in, out);
                thread.start();
            } catch (Exception e) {
                socket.close();
                e.printStackTrace();
            }
        }
    }
}
