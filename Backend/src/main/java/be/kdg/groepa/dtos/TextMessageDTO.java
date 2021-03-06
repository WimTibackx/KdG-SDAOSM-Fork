package be.kdg.groepa.dtos;

/**
 * Created by Tim on 3/03/14.
 */
public class TextMessageDTO {
    private int id;
    private String senderUsername;
    private String receiverUsername;
    private String messageBody;
    private String messageSubject;
    private boolean isRead;

    public TextMessageDTO(int id, String senderUsername, String receiverUsername, String messageBody, String messageSubject, boolean isRead) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.messageBody = messageBody;
        this.messageSubject = messageSubject;
        this.isRead = isRead;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
