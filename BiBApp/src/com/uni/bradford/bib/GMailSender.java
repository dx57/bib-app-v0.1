package com.uni.bradford.bib;

import javax.mail.Message;   
import javax.mail.PasswordAuthentication;   
import javax.mail.Session;   
import javax.mail.Transport;   
import javax.mail.internet.InternetAddress;   
import javax.mail.internet.MimeMessage;    
import java.util.Properties;   

public class GMailSender extends javax.mail.Authenticator 
{   
    private String mailhost = "smtp.gmail.com";   
    private String user;   
    private String password;   
    private Session session;   

    public GMailSender(String user, String password) 
    {   
        this.user = user;   
        this.password = password;   

        // Set properties for mailing
        Properties properties = new Properties();   
        properties.setProperty("mail.transport.protocol", "smtp");   
        properties.setProperty("mail.host", mailhost);   
        properties.put("mail.smtp.auth", "true");   
        properties.put("mail.smtp.port", "465");   
        properties.put("mail.smtp.socketFactory.port", "465");   
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
        properties.put("mail.smtp.socketFactory.fallback", "false");   
        properties.setProperty("mail.smtp.quitwait", "false");   

        session = Session.getDefaultInstance(properties, this);   
    }   

    protected PasswordAuthentication getPasswordAuthentication() 
    {   
        return new PasswordAuthentication(user, password);   
    }   

    public synchronized void sendMail(String recipient, String subject, String body) throws Exception 
    {   
    	// Setup email message
    	MimeMessage message = new MimeMessage(session);     
    	message.setSender(new InternetAddress(user));   
    	message.setSubject(subject);   
    	message.setText(body);
    	message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));   

    	Transport.send(message);   
    }   
}  
