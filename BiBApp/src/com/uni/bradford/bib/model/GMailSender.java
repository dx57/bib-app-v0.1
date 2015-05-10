package com.uni.bradford.bib.model;

import javax.mail.Message;   
import javax.mail.PasswordAuthentication;   
import javax.mail.Session;   
import javax.mail.Transport;   
import javax.mail.internet.InternetAddress;   
import javax.mail.internet.MimeMessage;    

import java.util.Properties;   

/**
 * Class to send mails automatically
 * 
 * @author Martin
 */
public class GMailSender extends javax.mail.Authenticator 
{   
	public static final String MAIL_HOST = "smtp.gmail.com";   
	
    private String user;   
    private String password;   
    private Session session;   

    /**
	 * Init mail communication channel
	 * 
	 * @param user Email address of the sender
	 * @param password Password of sender
	 */
    public GMailSender(String user, String password) 
    {   
        this.user = user;   
        this.password = password;   

        // Set properties for mailing
        Properties properties = new Properties();   
        properties.setProperty("mail.transport.protocol", "smtp");   
        properties.setProperty("mail.host", MAIL_HOST);   
        properties.put("mail.smtp.auth", "true");   
        properties.put("mail.smtp.port", "465");   
        properties.put("mail.smtp.socketFactory.port", "465");   
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
        properties.put("mail.smtp.socketFactory.fallback", "false");   
        properties.setProperty("mail.smtp.quitwait", "false");   

        session = Session.getDefaultInstance(properties, this);   
    }   

    /**
   	 * Password authentication process
   	 */
    protected PasswordAuthentication getPasswordAuthentication() 
    {   
        return new PasswordAuthentication(user, password);   
    }   

    /**
   	 * Send mail
   	 * 
   	 * @param recipient Email address of the receiver
   	 * @param subject Title of the mail
   	 * @param body Content of the mail
   	 */
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
