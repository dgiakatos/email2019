import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MailClient {
    public static void main(String[] args) throws Exception {
        String send = "";
        String received;
        try {
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("127.0.0.1", 1);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            while (true) {
                if (send.equals("LogIn")) {
                    System.out.println("----------");
                    System.out.println("MailSever:");
                    System.out.println("----------");
                    System.out.println("Type your username:");
                    System.out.println("==========");
                    send = scanner.nextLine();
                    out.writeUTF(send);
                    received = in.readUTF();
                    if (received.equals("true")) {
                        System.out.println("----------");
                        System.out.println("MailSever:");
                        System.out.println("----------");
                        System.out.println("Type your password:");
                        System.out.println("==========");
                        send = scanner.nextLine();
                        out.writeUTF(send);
                        received = in.readUTF();
                        if (received.equals("true")) {
                            connected();
                        } else {
                            System.out.println("----------");
                            System.out.println("MailSever:");
                            System.out.println("----------");
                            System.out.println("Wrong password.");
                        }
                    } else {
                        System.out.println("----------");
                        System.out.println("MailSever:");
                        System.out.println("----------");
                        System.out.println("Wrong username.");
                    }
                } else if (send.equals("Register")) {
                    System.out.println("----------");
                    System.out.println("MailSever:");
                    System.out.println("----------");
                    System.out.println("Create your username:");
                    System.out.println("==========");
                    send = scanner.nextLine();
                    out.writeUTF(send);
                    received = in.readUTF();
                    if (received.equals("false")) {
                        System.out.println("----------");
                        System.out.println("MailSever:");
                        System.out.println("----------");
                        System.out.println("The username already exists.");
                    } else {
                        System.out.println("----------");
                        System.out.println("MailSever:");
                        System.out.println("----------");
                        System.out.println("Type your password:");
                        System.out.println("==========");
                        send = scanner.nextLine();
                        out.writeUTF(send);
                        received = in.readUTF();
                        if (received.equals("true")) {
                            System.out.println("----------");
                            System.out.println("MailSever:");
                            System.out.println("----------");
                            System.out.println("The account created.");
                        }
                    }
                } else if (send.equals("Exit")) {
                    out.writeUTF(send);
                    socket.close();
                    break;
                } else {
                    System.out.println("----------");
                    System.out.println("MailSever:");
                    System.out.println("----------");
                    System.out.println("Hello, you connected as a guest.");
                    System.out.println("==========");
                    System.out.println("> LogIn");
                    System.out.println("> Register");
                    System.out.println("> Exit");
                    System.out.println("==========");
                    send = scanner.nextLine();
                    out.writeUTF(send);
                    //in.readUTF();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void connected() {
        System.out.println("Connected!");
    }
}
