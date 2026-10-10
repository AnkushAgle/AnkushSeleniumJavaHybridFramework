package com.aa.utility;

import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailUtils {

    public static void sendEmailWithAttachment(String to, String subject, String body, String filePath) {
//       
//    	boolean isEmailSkip=true;
//    	
//    	
//    	if(isEmailSkip) {
//    		
//    		return;
//    	}
//    	
    	
    	
        final String from = EnvironmentUtils.getValue("FROM_EMAIL");
        final String password = EnvironmentUtils.getValue("EMAIL_API_KEY");

        // SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authenticate session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject, "UTF-8");

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(
                    body.replaceAll("(?i)<br\\s*/?>", "\n").replaceAll("<[^>]*>", ""),
                    "UTF-8");

            MimeBodyPart htmlBodyPart = new MimeBodyPart();
            htmlBodyPart.setContent(body, "text/html; charset=UTF-8");

            MimeMultipart alternative = new MimeMultipart("alternative");
            alternative.addBodyPart(textBodyPart);
            alternative.addBodyPart(htmlBodyPart);

            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(alternative);

            // Attachment part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);

            if (filePath != null) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filePath);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(source.getName());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            // Send email
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
