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
                            connected(socket, in, out);
                            if (socket.isClosed()) {
                                break;
                            }
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

    private static void connected(Socket socket, DataInputStream in, DataOutputStream out) {
        String send = "";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                if (send.equals("NewEmail")) {
                    System.out.println("----------");
                    System.out.println("MailSever:");
                    System.out.println("----------");
                    System.out.println("Receiver:");
                    System.out.println("==========");
                    send = scanner.nextLine();
                    out.writeUTF(send);
                    if (in.readUTF().equals("true")) {
                        System.out.println("----------");
                        System.out.println("MailSever:");
                        System.out.println("----------");
                        System.out.println("Subject:");
                        System.out.println("==========");
                        send = scanner.nextLine();
                        out.writeUTF(send);
                        System.out.println("----------");
                        System.out.println("MailSever:");
                        System.out.println("----------");
                        System.out.println("Main body:");
                        System.out.println("==========");
                        send = scanner.nextLine();
                        out.writeUTF(send);
                        System.out.println("----------");
                        System.out.println("MailSever:");
                        System.out.println("----------");
                        System.out.println("Email sent successfully.");
                    } else {
                        System.out.println("The receiver does not exist.");
                    }
                } else if (send.equals("ShowEmails")) {
                    System.out.println("----------");
                    System.out.println("MailSever:");
                    System.out.println("----------");
                    System.out.println("Email list:");
                    System.out.println("----------");
                    System.out.println("Id\t\t\tFrom\t\t\tSubject");
                    try {
                        int size = Integer.parseInt(in.readUTF());
                        for (int i = 0; i < size; i++) {
                            System.out.print((i + 1) + ".\t");
                            if (in.readUTF().equals("true")) {
                                System.out.print("[New]\t");
                            }
                            System.out.print(in.readUTF() + "\t");
                            System.out.println(in.readUTF());
                        }
                    } catch (NumberFormatException e) {
                        e.fillInStackTrace();
                    }
                    System.out.println("==========");
                    System.out.println("> NewEmail");
                    System.out.println("> ShowEmails");
                    System.out.println("> ReadEmail");
                    System.out.println("> DeleteEmail");
                    System.out.println("> LogOut");
                    System.out.println("==========");
                    send = scanner.nextLine();
                    out.writeUTF(send);
                } else if (send.split(" ")[0].equals("ReadEmail")) {
                    System.out.println("----------");
                    System.out.println("MailSever:");
                    System.out.println("----------");
                    System.out.println("Reading email " + send.split(" ")[1] + ":");
                    System.out.println("----------");
                    System.out.println(in.readUTF());
                    System.out.println("==========");
                    System.out.println("> NewEmail");
                    System.out.println("> ShowEmails");
                    System.out.println("> ReadEmail");
                    System.out.println("> DeleteEmail");
                    System.out.println("> LogOut");
                    System.out.println("==========");
                    send = scanner.nextLine();
                    out.writeUTF(send);
                } else if (send.split(" ")[0].equals("DeleteEmail")) {
                    System.out.println("----------");
                    System.out.println("MailSever:");
                    System.out.println("----------");
                    System.out.println("Deleting email " + send.split(" ")[1] + ":");
                    System.out.println("----------");
                    System.out.println(in.readUTF());
                    System.out.println("==========");
                    System.out.println("> NewEmail");
                    System.out.println("> ShowEmails");
                    System.out.println("> ReadEmail");
                    System.out.println("> DeleteEmail");
                    System.out.println("> LogOut");
                    System.out.println("==========");
                    send = scanner.nextLine();
                    out.writeUTF(send);
                } else if (send.equals("LogOut")) {
                    break;
                } else if (send.equals("Exit")) {
                    out.writeUTF(send);
                    socket.close();
                    break;
                } else {
                    System.out.println("----------");
                    System.out.println("MailSever:");
                    System.out.println("----------");
                    System.out.println("Welcome back ");
                    System.out.println("==========");
                    System.out.println("> NewEmail");
                    System.out.println("> ShowEmails");
                    System.out.println("> ReadEmail");
                    System.out.println("> DeleteEmail");
                    System.out.println("> LogOut");
                    System.out.println("==========");
                    send = scanner.nextLine();
                    out.writeUTF(send);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
