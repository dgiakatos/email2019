import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MailClient {
    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("127.0.0.1", 1);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            while (true) {
                System.out.println(in.readUTF());
                String send = scanner.nextLine();
                out.writeUTF(send);
                if (send.equals("Exit")) {
                    System.out.println("Exiting...");
                    socket.close();
                    break;
                }
                System.out.println(in.readUTF());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
