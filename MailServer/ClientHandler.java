import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {
                out.writeUTF("MailServer");
                received = in.readUTF();
                if (received.equals("Exit")) {
                    System.out.println("Socket " + socket + " close.");
                    socket.close();
                    break;
                }
                out.writeUTF(LocalTime.now().toString());
                TimeUnit.SECONDS.sleep(5);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
