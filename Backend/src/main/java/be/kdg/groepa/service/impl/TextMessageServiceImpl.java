package be.kdg.groepa.service.impl;

import be.kdg.groepa.dtos.TextMessageDTO;
import be.kdg.groepa.model.TextMessage;
import be.kdg.groepa.persistence.api.TextMessageDao;
import be.kdg.groepa.service.api.TextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Tim on 27/02/14.
 */

@Service("textMessageService")
public class TextMessageServiceImpl implements TextMessageService {

    @Autowired
    private TextMessageDao textMessageDao;

    @Override
    public void addNewMessage(TextMessage message) {
        textMessageDao.addNewMessage(message);
    }

    @Override
    public List<TextMessageDTO> getReceivedMessagesByUser(int userId) {
        return textMessageDao.getReceivedMessagesByUser(userId);
    }

    @Override
    public List<TextMessageDTO> getSentMessagesByUser(int userId) {
        return textMessageDao.getSentMessagesByUser(userId);
    }
}
