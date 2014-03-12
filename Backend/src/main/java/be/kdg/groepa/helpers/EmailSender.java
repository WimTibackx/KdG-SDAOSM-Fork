package be.kdg.groepa.helpers;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
/**
 * Created by Tim on 8/03/14.
 */
public class EmailSender {
    private static void sendEmail(String dest, String sub, String body){
        
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");


        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("sd2014groepa@gmail.com", "software123!");
            }
        });

        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sd2014groepa@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dest));
            message.setSubject(sub);
            message.setText(body);
            Transport.send(message);
        
        
        } catch (MessagingException ex){
            System.out.println("Error occured: " + ex.getMessage());
        }
    }

    public static void sendPasswordResetEmail(String dest, String newPassword){
        sendEmail(dest, "Password reset", String.format("Dear user, \n\nYou have requested a password reset.\nYour new password is " + newPassword));
    }
}
