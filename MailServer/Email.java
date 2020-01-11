/**
 * @author Δημήτριος Παντελεήμων Γιακάτος
 * Η κλάση περιλαμβάνει τη δομή ενός μηνύματος.
 */
public class Email {
    private boolean isNew;
    private String sender;
    private String receiver;
    private String subject;
    private String mainBody;

    /**
     * Ο constructor αρχικοποεί τις μεταβλητές της κλάσης. Οι μεταβλητές τύπου String παίρνουν τιμή null και η μεταβλητή
     * τύπου boolean παίρνει τιμή false.
     */
    Email() {
        this.isNew = false;
        this.sender = null;
        this.receiver = null;
        this.subject = null;
        this.mainBody = null;
    }

    /**
     * Ο constructor ορίζει τις μεταβλητές της κλάσης.
     * @param isNew Υποδεικνύει αν το μήνυμα έχει ήδη διαβαστεί. true, αν είναι καινούργιο μήνυμα. false, αν δεν
     *              είναι καινούργιο το μήνυμα.
     * @param sender Ο αποστολέας του μηνύματος.
     * @param receiver Ο παραλήπτης του μηνύματος.
     * @param subject Το θέμα του μηνύματος.
     * @param mainBody Το κείμενο του μηνύματος.
     */
    Email(boolean isNew, String sender, String receiver, String subject, String mainBody) {
        this.isNew = isNew;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.mainBody = mainBody;
    }

    /**
     * @param isNew Υποδεικνύει αν το μήνυμα έχει ήδη διαβαστεί. true, αν είναι καινούργιο μήνυμα. false, αν δεν
     *              είναι καινούργιο το μήνυμα.
     */
    void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * @return Επιστρέφει αν το μήνυμα έχει ήδη διαβαστεί.
     */
    boolean getIsNew() {
        return isNew;
    }

    /**
     * @param sender Ο αποστολέας του μηνύματος.
     */
    void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return Επιστρέφει τον αποστολέα του μηνύματος.
     */
    String getSender() {
        return sender;
    }

    /**
     * @param receiver Ο παραλήπτης του μηνύματος.
     */
    void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * @return Επιστρέφει τον παραλήπτη του μηνύματος.
     */
    String getReceiver() {
        return receiver;
    }

    /**
     * @param subject Το θέμα του μηνύματος.
     */
    void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return Επιστρέφει το θέμα του μηνύματος.
     */
    String getSubject() {
        return subject;
    }

    /**
     * @param mainBody Το κείμενο του μηνύματος.
     */
    void setMainBody(String mainBody) {
        this.mainBody = mainBody;
    }

    /**
     * @return Επιστρέφει το κείμενο του μηνύματος.
     */
    String getMainBody() {
        return mainBody;
    }
}
