public class Email {
    private boolean isNew;
    private String sender;
    private String receiver;
    private String subject;
    private String mainBody;

    Email(boolean isNew, String sender, String receiver, String subject, String mainBody) {
        this.isNew = isNew;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.mainBody = mainBody;
    }

    void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    boolean getIsNew() {
        return isNew;
    }

    void setSender(String sender) {
        this.sender = sender;
    }

    String getSender() {
        return sender;
    }

    void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    String getReceiver() {
        return receiver;
    }

    void setSubject(String subject) {
        this.subject = subject;
    }

    String getSubject() {
        return subject;
    }

    void setMainBody(String mainBody) {
        this.mainBody = mainBody;
    }

    String getMainBody() {
        return mainBody;
    }
}
