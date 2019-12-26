import java.util.ArrayList;

public class Account {
    private String username;
    private String password;
    private ArrayList<Email> mailbox;

    Account() {
        this.username = null;
        this.password = null;
        this.mailbox = null;
    }

    Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.mailbox = null;
    }

    Account(String username, String password, ArrayList<Email> mailbox){
        this.username = username;
        this.password = password;
        this.mailbox = mailbox;
    }

    void setAccount(Account account) {
        this.username = account.username;
        this.password = account.password;
        this.mailbox = account.mailbox;
    }

    boolean isNull() {
        if (username == null && password == null && mailbox == null) {
            return true;
        }
        return false;
    }

    void setUsername(String username) { this.username = username; }

    String getUsername() {
        return username;
    }

    void setPassword(String password) { this.password = password; }

    String getPassword() {
        return password;
    }

    void setMailbox(ArrayList<Email> mailbox) { this.mailbox = mailbox; }

    ArrayList<Email> getMailbox() {
        return mailbox;
    }
}
