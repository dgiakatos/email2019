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
    private Account currentAccount = new Account();

    public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        while (true) {
            try {
                received = in.readUTF();
                //out.writeUTF("123");
                if (received.equals("LogIn") && currentAccount.isNull()) {
                    accountList.clear();
                    importAccounts();
                    logIn();
                } else if (received.equals("Register") && currentAccount.isNull()) {
                    register();
                    accountList.clear();
                    importAccounts();
                } else if (received.equals("Exit") && currentAccount.isNull()) {
                    exit();
                    break;
                } else if (received.equals("LogOut") && !currentAccount.isNull()) {
                    logOut();
                } else if (received.equals("ShowEmails") && !currentAccount.isNull()) {
                    showEmails();
                } else if (received.split(" ")[0].equals("ReadEmail") && !currentAccount.isNull()) {
                    readEmail(received.split(" ")[1]);
                } else if (received.split(" ")[0].equals("DeleteEmail") && !currentAccount.isNull()) {
                    deleteEmail(received.split(" ")[1]);
                } else if (received.equals("NewEmail") && !currentAccount.isNull()) {
                    newEmail();
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
                emailList.add(new Email(Boolean.parseBoolean(scanner.nextLine()), scanner.nextLine(), email, scanner.nextLine(), scanner.nextLine()));
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

    private void exportMailbox() {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter("MailServer/" + currentAccount.getUsername() + "_mailbox.txt"));
            for (Email email : currentAccount.getMailbox()) {
                file.write(email.getIsNew() + "\n");
                file.write(email.getSender() + "\n");
                file.write(email.getSubject() + "\n");
                file.write(email.getMainBody() + "\n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendMailBox(Email email) {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter("MailServer/" + email.getReceiver() + "_mailbox.txt", true));
            file.write(email.getIsNew() + "\n");
            file.write(email.getSender() + "\n");
            file.write(email.getSubject() + "\n");
            file.write(email.getMainBody() + "\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
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
                        currentAccount.setAccount(account);
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

    private void newEmail() {
        try {
            Email sentEmail = new Email();
            received = in.readUTF();
            boolean receiverExist = false;
            for (Account account : accountList) {
                if (account.getUsername().equals(received)) {
                    out.writeUTF("true");
                    receiverExist = true;
                    sentEmail.setIsNew(true);
                    sentEmail.setSender(currentAccount.getUsername());
                    sentEmail.setReceiver(account.getUsername());
                    received = in.readUTF();
                    sentEmail.setSubject(received);
                    received = in.readUTF();
                    sentEmail.setMainBody(received);
                    appendMailBox(sentEmail);
                    System.out.println("new email");
                    break;
                }
            }
            if (!receiverExist) {
                out.writeUTF("false");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEmails() {
        try {
            out.writeUTF(String.valueOf(currentAccount.getMailbox().size()));
            for (Email email : currentAccount.getMailbox()) {
                out.writeUTF(String.valueOf(email.getIsNew()));
                out.writeUTF(email.getSender());
                out.writeUTF(email.getSubject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readEmail(String emailId) {
        try {
            if (Integer.parseInt(emailId)<=0 || Integer.parseInt(emailId)>currentAccount.getMailbox().size()) {
                out.writeUTF("Wrong email id.");
            }
            currentAccount.getMailbox().get(Integer.parseInt(emailId)-1).setIsNew(false);
            out.writeUTF(currentAccount.getMailbox().get(Integer.parseInt(emailId)-1).getMainBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteEmail(String emailId) {
        try {
            if (Integer.parseInt(emailId)<=0 || Integer.parseInt(emailId)>currentAccount.getMailbox().size()) {
                out.writeUTF("Wrong email id.");
            }
            currentAccount.getMailbox().remove(Integer.parseInt(emailId)-1);
            out.writeUTF("Email deleted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logOut() {
        exportMailbox();
        Account account = new Account();
        currentAccount.setAccount(account);
    }

    private void exit() {
            System.out.println("Socket " + socket + " close.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
