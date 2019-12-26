import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<Account> accountList = new ArrayList<>();
    private String received;

    public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        importAccounts();
    }

    @Override
    public void run() {
        while (true) {
            try {
                received = in.readUTF();
                if (received.equals("LogIn")) {
                    logIn();
                } else if (received.equals("SignIn")) {
                    register();
                } else if (received.equals("Exit")) {
                    exit();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void importAccounts() {
        try {
            File file = new File("MailServer/accounts.txt");
            Scanner scanner = new Scanner(file);
            String username;
            String password;
            while (scanner.hasNextLine()) {
                username = scanner.nextLine();
                password = scanner.nextLine();
                accountList.add(new Account(username, password, importAccountMail(username)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Email> importAccountMail(String email) {
        ArrayList<Email> emailList = new ArrayList<>();
        try {
            File file = new File("MailServer/".concat(email.concat("_mailbox.txt")));
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                emailList.add(new Email(Boolean.parseBoolean(scanner.nextLine()), email, scanner.nextLine(), scanner.nextLine(), scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return emailList;
    }

    private void register() {}

    private void logIn() {
        try {
            received = in.readUTF();
            for (Account account : accountList) {
                if (account.getUsername().equals(received)) {
                    out.writeUTF("true");
                    received = in.readUTF();
                    if (account.getPassword().equals(received)) {
                        out.writeUTF("true");
                    } else {
                        out.writeUTF("false");
                    }
                } else {
                    out.writeUTF("false");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newEmail() {}

    private void showEmails() {}

    private void readEmails() {}

    private void deleteEmails() {}

    private void logOut() {}

    private void exit() {
            System.out.println("Socket " + socket + " close.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
