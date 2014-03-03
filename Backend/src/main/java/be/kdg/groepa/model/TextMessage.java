package be.kdg.groepa.model;

import javax.persistence.*;

/**
 * Created by Tim on 27/02/14.
 */
@Entity
@Table(name="t_textmessage")
public class TextMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="senderId", nullable = true)
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiverId", nullable = true)
    private User receiver;

    @Column
    private String subject;

    @Column
    private String messageBody;

    @Column
    private boolean isRead;

    public TextMessage(){}

    public TextMessage(User sender, User receiver, String subject, String body){
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.messageBody = body;
        this.isRead = false;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getSubject() {
        return subject;
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
}
