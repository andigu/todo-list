package services;

import model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * @author Susheel Kona
 */
public class EmailService {
    private static final EmailService instance = new EmailService();
    public static EmailService getInstance() {return instance;}

    final String username = "todolist.team@gmail.com";
    final String password = "jeffdean"; //Bad Security

    Properties properties = new Properties(){{
        put("mail.smtp.auth", "true");
        put("mail.smtp.starttls.enable", "true");
        put("mail.smtp.host", "smtp.gmail.com");
        put("mail.smtp.port", "587");
        put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }};

    public void send(String body, String subject, String to) {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session){{
                setFrom(new InternetAddress(username));
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                setSubject(subject);
                setText(body);
            }};
            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Temporary
    public void sendWelcomeMessage(User user) {
        String body = "Welcome to TODOLIST, "+user.getName();
        send(body, "Welcome to TODOLIST", user.getEmail());
    }


}