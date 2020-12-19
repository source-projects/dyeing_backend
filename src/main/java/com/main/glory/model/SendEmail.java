package com.main.glory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;


@NoArgsConstructor
@Getter
@Setter
public class SendEmail {

    String to;
    String  fileName;
    String subject;
    String text;
    String username ="mohan.glorygfl@gmail.com";
    String password = "Mohan_123";


    public SendEmail(String to, String fileName, String subject,String text) {
        this.to = to;
        this.fileName = fileName;
        this.subject = subject;
        this.text=text;
    }

    public void sendMail()
    {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(this.to)
            );
            message.setSubject(this.subject);

            //message.setText(this.text);


            MimeBodyPart messageBodyPart = new MimeBodyPart();

            Multipart multipart = new MimeMultipart();

            //set file
            DataSource source = new FileDataSource(this.fileName);
            messageBodyPart.setText(this.getText());
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(this.fileName);
            multipart.addBodyPart(messageBodyPart);

            //set text
            messageBodyPart=new MimeBodyPart();
            messageBodyPart.setText(this.getText());
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done:"+this.getTo());

        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
}
