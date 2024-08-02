package com.example.university.email;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendSimpleMessage(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("tt");
        message.setText("ttt");
        emailSender.send(message);
    }


    @Async
    public void sendPasswordChangeMail(String userEmail, String token) throws MessagingException, UnsupportedEncodingException {
        Properties mailProperties = setMailProperties();
        Session session = Session.getDefaultInstance(mailProperties);
        MimeMessage message = new MimeMessage(session);
        constructPasswordChangeMail(message, userEmail, token);

        emailSender.send(message);
    }

    @Async
    public void sendAdminMail(String userEmail, String username, String password) throws MessagingException, UnsupportedEncodingException {
        Properties mailProperties = setMailProperties();
        Session session = Session.getDefaultInstance(mailProperties);
        MimeMessage message = new MimeMessage(session);
        constructAdminMail(message, userEmail, username, password);

        emailSender.send(message);
    }


    private void constructPasswordChangeMail(MimeMessage mimeMessage, String userEmail, String token)
            throws MessagingException, UnsupportedEncodingException {
        String changePasswordLink = createLink(token);
        String mailText = "<p>Poštovani,</p>" +
                "<br>" +
                "<p>stranici za promjenu lozinke možete pristupiti putem sljedećeg <a href=\"" + changePasswordLink + "\">linka</a>.</p>" +
                "<br>" +
                "<p>Link je aktivan naredna 24 sata. Ako ne uspijete promijeniti lozinku za to vrijeme," +
                " nakon isteka linka moraćete kliknuti na \"Ponovo pošalji\", kako biste dobili novi link. </p>" +
                "<br>" +
                "<p>Ako Vam je potrebna tehnička podrška, molimo Vas da se obratite administratorima na mail universityinfo@gmail.com</p>" +
                "<br>" +
                "<p>S poštovanjem,</p>" +
                "<p>Vaš UniTeam</p>";
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
        mimeMessage.setFrom(new InternetAddress("cecajezakon86@gmail.com", "University@Info"));
        mimeMessage.setSubject("Link za promjenu lozinke");
        mimeMessage.setText(mailText,"utf-8", "html");
    }

    private void constructAdminMail(MimeMessage mimeMessage, String userEmail, String username, String randomPassword)
            throws MessagingException, UnsupportedEncodingException {
        String pageLink = "http://localhost:3000/";
        String mailText = "<p>Poštovani,</p>" +
                "<br>" +
                "<p>Poslali ste administrator na  <a href=\"" + pageLink + "\">stranici</a>.</p>" +
                "<br>" +
                "<p>Vaše korisničko ime je: " + username + ", a Vaša nasumično generisana lozinka je:</p>" + randomPassword +
                "<br>"+
                "<p>Lozinku možete promijeniti nakon što se ulogujete i u korisničkom meniju odaberete opciju \"Izmjena lozinke\".</p>" +
                "<br>" +
                "<p>Ako Vam je potrebna tehnička podrška, molimo Vas da se obratite administratorima na mail universityinfo@gmail.com</p>" +
                "<br>" +
                "<p>S poštovanjem,</p>" +
                "<p>Vaš UniTeam</p>";
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
        mimeMessage.setFrom(new InternetAddress("cecajezakon86@gmail.com", "University@Info"));
        mimeMessage.setSubject("Administracija");
        mimeMessage.setText(mailText,"utf-8", "html");
    }



    private String createLink(String token) {
        return "http://localhost:3000/validate?token=" + token;
    }

    private Properties setMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }


}
