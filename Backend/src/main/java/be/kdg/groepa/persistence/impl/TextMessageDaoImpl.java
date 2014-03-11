package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.dtos.TextMessageDTO;
import be.kdg.groepa.model.TextMessage;
import be.kdg.groepa.persistence.api.TextMessageDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 27/02/14.
 */
@SuppressWarnings("JpaQlInspection")
@Repository("textMessageDao")
public class TextMessageDaoImpl implements TextMessageDao{

    @Override
    public void addNewMessage(TextMessage message) {
        HibernateUtil.addObject(message);
    }

    @Override
    public List<TextMessageDTO> getReceivedMessagesByUser(int userId) {
        Session ses = HibernateUtil.openSession();
        List<TextMessageDTO> toReturn = new ArrayList<TextMessageDTO>();
        Query query = ses.createQuery("from TextMessage tm where tm.receiver.id = :id");
        query.setInteger("id", userId);
        List<TextMessage> messages = (List<TextMessage>)query.list();
        // Get the routes of the sender and receiver to counter lazyInitializationExceptions
        for(TextMessage m:messages){
            //m.getSender().getRoutes().size();
            //m.getReceiver().getRoutes().size();
            toReturn.add(new TextMessageDTO(m.getId(), m.getSender().getUsername(), m.getReceiver().getUsername(), m.getMessageBody(), m.getSubject(), m.isRead()));
        }
        HibernateUtil.closeSession(ses);
        return toReturn;
    }

    @Override
    public List<TextMessageDTO> getSentMessagesByUser(int userId) {
        Session ses = HibernateUtil.openSession();
        List<TextMessageDTO> toReturn = new ArrayList<TextMessageDTO>();
        Query query = ses.createQuery("from TextMessage tm where tm.sender.id = :id");
        query.setInteger("id", userId);
        List<TextMessage> messages = (List<TextMessage>)query.list();
        // Get the routes of the sender and receiver to counter lazyInitializationExceptions
        for(TextMessage m:messages){
            //m.getSender().getRoutes().size();
            //m.getReceiver().getRoutes().size();
            toReturn.add(new TextMessageDTO(m.getId(), m.getSender().getUsername(), m.getReceiver().getUsername(), m.getMessageBody(), m.getSubject(), m.isRead()));
        }
        HibernateUtil.closeSession(ses);
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

