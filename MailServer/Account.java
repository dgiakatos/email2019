import java.util.ArrayList;

/**
 * @author Δημήτριος Παντελεήμων Γιακάτος
 * Η κλάση περιλαμβάνει την δομή ενός λογαριασμού.
 */
public class Account {
    private String username;
    private String password;
    private ArrayList<Email> mailbox;

    /**
     * Ο constructor αρχικοποεί τις μεταβλητές σε null.
     */
    Account() {
        this.username = null;
        this.password = null;
        this.mailbox = null;
    }

    /**
     * Ο constructor ορίζει τις μεταβλητές της κλάσης.
     * @param username Το όνομα χρήστη.
     * @param password Ο κωδικός χρήστη.
     */
    Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.mailbox = null;
    }

    /**
     * Ο constructor ορίζει τις μεταβλητές της κλάσης.
     * @param username Το όνομα χρήστη.
     * @param password Ο κωδικός χρήστη.
     * @param mailbox Το γραμματοκιβώτιο του χρήστη.
     */
    Account(String username, String password, ArrayList<Email> mailbox){
        this.username = username;
        this.password = password;
        this.mailbox = mailbox;
    }

    /**
     * Ο constructor ορίζει τις μεταβλητές της κλάσης.
     * @param account Αντικείμενο της κλάσης Account.
     */
    void setAccount(Account account) {
        this.username = account.username;
        this.password = account.password;
        this.mailbox = account.mailbox;
    }

    /**
     * @return Επιστρέφει true αν όλες οι μεταβλητές έχουν τιμή null, αλλιώς επιστρέφει false.
     */
    boolean isNull() {
        if (username == null && password == null && mailbox == null) {
            return true;
        }
        return false;
    }

    /**
     * @param username Το όνομα χρήστη.
     */
    void setUsername(String username) { this.username = username; }

    /**
     * @return Επιστρέφει το όνομα χρήστη.
     */
    String getUsername() {
        return username;
    }

    /**
     * @param password Ο κωδικός χρήστη.
     */
    void setPassword(String password) { this.password = password; }

    /**
     * @return Επιστρέφει το κωδικό χρήστη.
     */
    String getPassword() {
        return password;
    }

    /**
     * @param mailbox Το γραμματοκιβώτιο του χρήστη.
     */
    void setMailbox(ArrayList<Email> mailbox) { this.mailbox = mailbox; }

    /**
     * @return Το γραμματοκιβώτιο του χρήστη, το οποίο είναι μία λίστα απο
     * Emails.
     */
    ArrayList<Email> getMailbox() {
        return mailbox;
    }
}
