package be.kdg.groepa.model;

import org.threeten.bp.LocalDateTime;

import javax.jms.Session;
import javax.persistence.*;

/**
 * Created by Thierry on 6/02/14.
 */
@Entity
@Table(name="t_session")
public class SessionObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="sessionToken")
    private String sessionToken;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    @Column(name="expirationDate")
    private LocalDateTime experiationDate;

    public SessionObject(){ }

    public SessionObject(User user) {
        this.user = user;
        this.experiationDate = LocalDateTime.now().plusDays(1L);
        this.sessionToken = user.getUsername() + "123456";

        //this.sessionToken = UUID.randomUUID().toString();
    }


    public String getSessionToken() {
        return sessionToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExperiationDate() {
        return experiationDate;
    }

    public void setExperiationDate(LocalDateTime experiationDate) {
        this.experiationDate = experiationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionObject that = (SessionObject) o;

        return !(sessionToken != null ? !sessionToken.equals(that.sessionToken) : that.sessionToken != null);

    }

    @Override
    public int hashCode() {
        return sessionToken != null ? sessionToken.hashCode() : 0;
    }
}
