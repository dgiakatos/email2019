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
                //out.writeUTF("123");
                if (received.equals("LogIn")) {
                    logIn();
                } else if (received.equals("Register")) {
                    register();
                    accountList.clear();
                    importAccounts();
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
            BufferedReader file = new BufferedReader(new FileReader("MailServer/accounts.txt"));
            Scanner scanner = new Scanner(file);
            String username;
            String password;
            while (scanner.hasNextLine()) {
                username = scanner.nextLine();
                password = scanner.nextLine();
                accountList.add(new Account(username, password, importAccountMail(username)));
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Email> importAccountMail(String email) {
        ArrayList<Email> emailList = new ArrayList<>();
        try {
            BufferedReader file = new BufferedReader(new FileReader("MailServer/".concat(email.concat("_mailbox.txt"))));
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                emailList.add(new Email(Boolean.parseBoolean(scanner.nextLine()), email, scanner.nextLine(), scanner.nextLine(), scanner.nextLine()));
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emailList;
    }

    private boolean exportAccount(Account account) {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter("MailServer/accounts.txt", true));
            file.write(account.getUsername() + "\n");
            file.write(account.getPassword() + "\n");
            file.close();
            file = new BufferedWriter(new FileWriter("MailServer/" + account.getUsername() + "_mailbox.txt"));
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void register() {
        try {
            String username = in.readUTF();
            boolean usernameUnique = false;
            boolean accountCreated = false;
            for (Account account : accountList) {
                if (account.getUsername().equals(username)) {
                    out.writeUTF(String.valueOf(usernameUnique));
                    return;
                }
            }
            usernameUnique = true;
            out.writeUTF(String.valueOf(usernameUnique));
            String password = in.readUTF();
            if (exportAccount(new Account(username, password))) {
                accountCreated = true;
                out.writeUTF(String.valueOf(accountCreated));
            } else {
                out.writeUTF(String.valueOf(accountCreated));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logIn() {
        try {
            received = in.readUTF();
            boolean usernameFound = false;
            boolean passwordFound = false;
            for (Account account : accountList) {
                if (account.getUsername().equals(received)) {
                    usernameFound = true;
                    out.writeUTF(String.valueOf(usernameFound));
                    received = in.readUTF();
                    if (account.getPassword().equals(received)) {
                        passwordFound = true;
                        out.writeUTF(String.valueOf(passwordFound));
                    } else {
                        out.writeUTF(String.valueOf(passwordFound));
                    }
                    break;
                }
            }
            if (!usernameFound) {
                out.writeUTF(String.valueOf(usernameFound));
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
