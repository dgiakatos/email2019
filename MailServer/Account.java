import java.util.ArrayList;

public class Account {
    private String username;
    private String password;
    private ArrayList<Email> mailbox;

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

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    ArrayList<Email> getMailbox() {
        return mailbox;
    }
}
