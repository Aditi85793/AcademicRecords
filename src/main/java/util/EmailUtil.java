package util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

public class EmailUtil {

    private static final String FROM_EMAIL = "yadavaditi7454@gmail.com";
    private static final String APP_PASSWORD = "smdfoflmeznoekrz"; // Gmail App Password

    // 🔹 SESSION METHOD (YEH MISSING THA)
    private static Session getSession() {

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            }
        );
    }

    // 🔹 SIMPLE EMAIL
    public static void sendEmail(String toEmail, String subject, String messageText) {

        try {
            Session session = getSession();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("✅ EMAIL SENT");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 EMAIL WITH PDF ATTACHMENT
    public static void sendEmailWithAttachment(
            String toEmail,
            String subject,
            String messageText,
            String pdfPath) {

        try {
            Session session = getSession();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);

            // Text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(messageText);

            // PDF part
            MimeBodyPart pdfPart = new MimeBodyPart();
            DataSource source = new FileDataSource(pdfPath);
            pdfPart.setDataHandler(new DataHandler(source));
            pdfPart.setFileName("StudentDetails.pdf");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(pdfPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("✅ EMAIL WITH PDF SENT");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendSimpleEmail(String to, String subject, String message) {

        try {
            Session session = getSession(); // 🔹 agar tumhare util me pehle se hai

            Message msg = new MimeMessage(session);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
