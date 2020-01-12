import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Δημήτριος Παντελεήμων Γιακάτος
 * Η κλάση εξυπηρετεί τα αιτήματα ενός πελάτη.
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<Account> accountList = new ArrayList<>();
    private String received;
    private Account currentAccount = new Account();

    /**
     * Ο constructor της κλάσης.
     * @param socket Το socket της εισερχόμενης αίτησης.
     * @param in Το αίτημα που λαμβάνει από τον πελάτη.
     * @param out Τη πληροφορία που θα στείλει στον πελάτη.
     */
    public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    /**
     * Η μέθοδος εκτελείται συνέχεια μέχρι ο πελάτης να τερματήσει την σύνδεση με τον εξυπηρετητή.
     * Εκτελεί κάποια λειτουργεία του εξυπηρετητή μέτα από κάποιο αίτημα που έχει λάβει από τον πελάτη.
     */
    @Override
    public void run() {
        while (true) {
            try {
                received = in.readUTF();
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
                    currentAccount.getMailbox().clear();
                    currentAccount.setMailbox(importAccountMail(currentAccount.getUsername()));
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

    /**
     * Φορτώνει τους λαγαριασμούς της υπηρεσίας mail σε μία δομή δοδομένων ArrayList.
     */
    private void importAccounts() {
        try {
            BufferedReader file = new BufferedReader(new FileReader("ServerFiles/accounts.txt"));
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

    /**
     * Φορτώνει την αλληλοηραφία ενός λογαριασμού σε μία δομή δεδομένων ArrayList.
     * @param email Το email του γραμματοκιβωτίου που θέλει να φορτώσει.
     * @return Το γραμματοκιβώτιο με όλη την αλληλογραφία (emails).
     */
    private ArrayList<Email> importAccountMail(String email) {
        ArrayList<Email> emailList = new ArrayList<>();
        try {
            BufferedReader file = new BufferedReader(new FileReader("ServerFiles/".concat(email.concat("_mailbox.txt"))));
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

    /**
     * Εξάγει τις τιμές των μεταβλητών username και password από την κλάση Account (το username και το password του
     * συγκεκριμένου λογαριασμού) και δημιουργεί ένα γραμματοκιβώτιο για το username, ώστε να μπορεί
     * να αποθηκεύει την αλληλογραφία.
     * @param account Αντικείμενο της κλάσης Account.
     * @return Επισρέφει true αν η εξαγωγή του λαγαριασμού και η δημιουργία του γραμματοκιβωτίου είναι επιτυχής, αλλιώς
     * επιστρέφει false.
     */
    private boolean exportAccount(Account account) {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter("ServerFiles/accounts.txt", true));
            file.write(account.getUsername() + "\n");
            file.write(account.getPassword() + "\n");
            file.close();
            file = new BufferedWriter(new FileWriter("ServerFiles/" + account.getUsername() + "_mailbox.txt"));
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Εξάγει την αλληλογραφία του λαγαρισμού σε ένα αρχείο username_mailbox.txt, που το username αντιστοιχεί στην τιμή
     * του ονόματος χρήστη.
     */
    private void exportMailbox() {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter("ServerFiles/" + currentAccount.getUsername() + "_mailbox.txt"));
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

    /**
     * Προσθέτει στο γραμματοκιβώτιο την αλληλογραφία του χρήστη που μόλις έχει ληφθεί από τον εξυπηρετητή.
     * @param email Το μήνυμα (email) που θα προσθεθεί στο γραμματοκιβώτιο.
     */
    private void appendMailBox(Email email) {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter("ServerFiles/" + email.getReceiver() + "_mailbox.txt", true));
            file.write(email.getIsNew() + "\n");
            file.write(email.getSender() + "\n");
            file.write(email.getSubject() + "\n");
            file.write(email.getMainBody() + "\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Η λειτουργία αυτή θα επιτρέπει την δημιουργία νέου λογαριασμού
     * email. Δεν θα επιτρέπεται η δημιουργία λογαριασμού, εφόσον το
     * username είναι ήδη καταχωρημένο.
     */
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

    /**
     * Η λειτουργία αυτή πιστοποιεί τον χρήστη. Σε περίπτωση
     * λανθασμένου κωδικού ή ονόματος χρήστη θα επιστρέφεται κάποιο
     * ενδεικτικό μήνυμα (π.χ “Invalid user or password”). Σε περίπτωση
     * επιτυχούς πιστοποίησης ο χρήστης θα έχει πρόσβαση στις
     * επόμενες λειτουργίες.
     */
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

    /**
     * Η λειτουργία αυτή δημιουργεί ένα νέο Email συμπληρωμένο από
     * τον χρήστη και το στέλνει στο γραμματοκιβώτιο του παραλήπτη.
     * Σε περίπτωση που ο παραλήπτης δεν είναι έγκυρος χρήστης, το
     * Email θα απορρίπτεται και θα επιστρέφεται ένα ενδεικτικό μήνυμα.
     */
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

    /**
     * Η λειτουργία αυτή θα εμφανίζει τo γραμματοκιβώτιο του χρήστη,
     * προβάλλοντας μία προεπισκόπηση του κάθε Email και το id του,
     * προκειμένου ο χρήστης να είναι σε θέση να αναφερθεί σε κάθε
     * Email.
     */
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

    /**
     * Η λειτουργία αυτή θα δέχεται ως όρισμα το id ενός Email και θα το
     * προβάλλει ολοκληρωμένο στον χρήστη. Σε περίπτωση που το Email
     * ήταν αδιάβαστο, το πεδίο isNew θα γίνεται false. Σε περίπτωση
     * λανθασμένου id θα επιστρέφεται ένα ενδεικτικό μήνυμα.
     * @param emailId Το id του μηνύματος που θέλει να διαβάσει.
     */
    private void readEmail(String emailId) {
        try {
            if (Integer.parseInt(emailId)<=0 || Integer.parseInt(emailId)>currentAccount.getMailbox().size()) {
                out.writeUTF("Wrong email id.");
            }
            currentAccount.getMailbox().get(Integer.parseInt(emailId)-1).setIsNew(false);
            out.writeUTF(currentAccount.getMailbox().get(Integer.parseInt(emailId)-1).getMainBody());
            exportMailbox();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Η λειτουργία αυτή θα δέχεται ως όρισμα το id ενός Email και θα το
     * διαγράφει από το γραμματοκιβώτιο του χρήστη. Σε περίπτωση
     * λανθασμένου id θα επιστρέφεται ένα ενδεικτικό μήνυμα.
     * @param emailId Το id του μηνύματος που θέλει να διαγράψει.
     */
    private void deleteEmail(String emailId) {
        try {
            if (Integer.parseInt(emailId)<=0 || Integer.parseInt(emailId)>currentAccount.getMailbox().size()) {
                out.writeUTF("Wrong email id.");
            }
            currentAccount.getMailbox().remove(Integer.parseInt(emailId)-1);
            out.writeUTF("Email deleted.");
            exportMailbox();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Η λειτουργία αυτή θα αποσυνδέει τον χρήστη από τον λογαριασμό
     * του και θα τον επιστρέφει στο αρχικό μενού επιλογών.
     */
    private void logOut() {
        Account account = new Account();
        currentAccount.setAccount(account);
    }

    /**
     * Η λειτουργία αυτή θα τερματίζει την σύνδεση του πελάτη με τον
     * εξυπηρετητή και θα απελευθερώνει τους πόρους του συστήματος.
     */
    private void exit() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
