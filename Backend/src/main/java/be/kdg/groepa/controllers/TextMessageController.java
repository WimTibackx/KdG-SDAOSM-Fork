package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.TextMessageDTO;
import be.kdg.groepa.model.TextMessage;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.TextMessageService;
import be.kdg.groepa.service.api.UserService;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Tim on 27/02/14.
 */
@Controller
@RequestMapping(value = "/authorized/textmessage")
public class TextMessageController extends BaseController{

    @Autowired
    private TextMessageService textMessageService;

    @Autowired
    private UserService userService;

    @RequestMapping(value="/send", method= RequestMethod.POST)
    public @ResponseBody
    String addTextMessage(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject myJson = new JSONObject();
        JSONObject dataOb = new JSONObject(data);

        String senderUsername, receiverUsername, message, subject;

        senderUsername = dataOb.getString("senderUsername");
        receiverUsername = dataOb.getString("receiverUsername");
        message = dataOb.getString("messageBody");
        subject = dataOb.getString("messageSubject");

        User sender = null;
        User receiver = null;

            sender = userService.getUser(senderUsername);
            receiver = userService.getUser(receiverUsername);
        if(receiver == null){
            myJson.put("error", "Receiver not found.");
            return myJson.toString();
        }

        TextMessage textMessage = new TextMessage(sender, receiver, subject, message);

        try{
            textMessageService.addNewMessage(textMessage);
        } catch (Exception e){
            myJson.put("error", "Message not sent: error occured.");
            return myJson.toString();
        }
        myJson.put("result", "Message succesfully sent.");
        return myJson.toString();
    }

    @Transactional
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public @ResponseBody String getMessagesFromUser(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
        JSONObject myJson = new JSONObject();
        Gson gson = new Gson();

        User user = userService.getUserById(id);
        if (user == null) {
            myJson.put("error","UserDoesNotExist");
            return myJson.toString();
        }

        List<TextMessageDTO> sentMessageList = textMessageService.getSentMessagesByUser(id);
        List<TextMessageDTO> receivedMessageList = textMessageService.getReceivedMessagesByUser(id);
        JSONArray sentMessages = new JSONArray(sentMessageList);
        JSONArray receivedMessages = new JSONArray(receivedMessageList);
        myJson.put("sentMessages", sentMessages);
        myJson.put("receivedMessages", receivedMessages);
        myJson.put("result", "Messages succesfully retrieved.");


        return myJson.toString();

    }

    @RequestMapping(value="/read", method= RequestMethod.POST)
    public @ResponseBody void setMessageRead(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject dataOb = new JSONObject(data);
        int messageId;
        messageId = dataOb.getInt("messageId");
        textMessageService.readMessage(messageId);
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody String getMessagesFromUser(HttpServletRequest request, HttpServletResponse response) {
        JSONObject myJson = new JSONObject();
        Gson gson = new Gson();


        User user = this.getCurrentUser(request);
        if (user == null) {
            myJson.put("error","UserDoesNotExist");
            return myJson.toString();
        }
        List<TextMessageDTO> sentMessageList = textMessageService.getSentMessagesByUser(user.getId());
        List<TextMessageDTO> receivedMessageList = textMessageService.getReceivedMessagesByUser(user.getId());
        JSONArray sentMessages = new JSONArray(sentMessageList);
        JSONArray receivedMessages = new JSONArray(receivedMessageList);
        myJson.put("sentMessages", sentMessages);
        myJson.put("receivedMessages", receivedMessages);
        myJson.put("result", "Messages succesfully retrieved.");

        return myJson.toString();

    }


}
