package util;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    private static final String FROM_EMAIL = "yadavaditi7454@gmail.com";
    private static final String APP_PASSWORD = "smdfoflmeznoekrz"; // 16-digit app password

    public static void sendEmail(String toEmail, String subject, String messageText) {

        Properties props = new Properties();
         props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);

            System.out.println("EMAIL SENT SUCCESSFULLY TO: " + toEmail);

        } catch (Exception e) {
            System.out.println(" EMAIL FAILED");
            e.printStackTrace();
        }
    }
}
