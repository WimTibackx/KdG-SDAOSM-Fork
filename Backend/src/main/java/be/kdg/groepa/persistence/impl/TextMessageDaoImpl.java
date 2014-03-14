package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.dtos.TextMessageDTO;
import be.kdg.groepa.model.TextMessage;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.TextMessageDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tim on 27/02/14.
 */
@SuppressWarnings("JpaQlInspection")
@Repository("textMessageDao")
public class TextMessageDaoImpl implements TextMessageDao{

    @Transactional
    @Override
    public void addNewMessage(TextMessage message) {
        Session ses = HibernateUtil.openSession();
        Query q = ses.createQuery("from User u where u.id = :id");
        q.setInteger("id", message.getSender().getId());
        User sender = (User)q.uniqueResult();
        sender.getInbox().add(message);
        ses.saveOrUpdate(sender);

        q.setInteger("id", message.getReceiver().getId());
        User receiver = (User)q.uniqueResult();
        receiver.getOutbox().add(message);
        ses.saveOrUpdate(receiver);

        ses.saveOrUpdate(message);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public List<TextMessageDTO> getReceivedMessagesByUser(int userId) {
        Session ses = HibernateUtil.openSession();
        List<TextMessageDTO> toReturn = new ArrayList<TextMessageDTO>();
        Query query = ses.createQuery("from User u where u.id = :id order by u.id");
        query.setInteger("id", userId);
        User user = (User)query.uniqueResult();
        // Get the routes of the sender and receiver to counter lazyInitializationExceptions
        for(TextMessage m:user.getInbox()){
            toReturn.add(new TextMessageDTO(m.getId(), m.getSender().getUsername(), m.getReceiver().getUsername(), m.getMessageBody(), m.getSubject(), m.isRead()));
        }
        HibernateUtil.closeSession(ses);
        // A reverse causes the messages to be returned sorted by most --> least recent
        Collections.reverse(toReturn);
        return toReturn;
    }

    @Override
    public List<TextMessageDTO> getSentMessagesByUser(int userId) {
        Session ses = HibernateUtil.openSession();
        List<TextMessageDTO> toReturn = new ArrayList<TextMessageDTO>();
        Query query = ses.createQuery("from User u where u.id = :id order by u.id");
        query.setInteger("id", userId);
        User user = (User)query.uniqueResult();
        // Get the routes of the sender and receiver to counter lazyInitializationExceptions
        for(TextMessage m:user.getOutbox()){
            toReturn.add(new TextMessageDTO(m.getId(), m.getSender().getUsername(), m.getReceiver().getUsername(), m.getMessageBody(), m.getSubject(), m.isRead()));
        }
        HibernateUtil.closeSession(ses);
        // A reverse causes the messages to be returned sorted by most --> least recent
        Collections.reverse(toReturn);
        return toReturn;
    }

    @Override
    public void readMessage(int messageId) {
        Session ses = HibernateUtil.openSession();
        TextMessage msg = (TextMessage)ses.get(TextMessage.class, messageId);
        msg.setRead(true);
        ses.saveOrUpdate(msg);
        HibernateUtil.closeSession(ses);
    }
}

