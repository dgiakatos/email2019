import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Δημήτριος Παντελεήμων Γιακάτος
 * Η κλάση δημιουργεί έναν mail server που μπορούν να συνδεθούν ταυτόχρονα αρκετοί πελάτες (clients).
 * Κάθε πελάτης που συνδέεται στον εξυπηρετητή (server) στέλνεται σε ένα thread που δημιουγείται από τον server.
 */
public class MailServer {
    /**
     * Η μέθοδος ανοίγει ένα server socket μία port και για κάθε καινούργιο πελάτη δημιουργεί ένα thread.
     * @param args Τη port του εξυπηρετητή.
     * @throws IOException Κάποιο μήνυμα με βάση το σφάλμα που έχει συμβεί.
     */
    public static void main(String[] args) throws IOException {
        ServerSocket mailServer = new ServerSocket(Integer.parseInt(args[0]));
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
