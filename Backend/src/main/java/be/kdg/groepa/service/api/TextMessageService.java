package be.kdg.groepa.service.api;

import be.kdg.groepa.dtos.TextMessageDTO;
import be.kdg.groepa.model.TextMessage;

import java.util.List;

/**
 * Created by Tim on 27/02/14.
 */
public interface TextMessageService {

    public void addNewMessage(TextMessage message);

    public List<TextMessageDTO> getReceivedMessagesByUser(int userId);

    public List<TextMessageDTO> getSentMessagesByUser(int userId);
}
