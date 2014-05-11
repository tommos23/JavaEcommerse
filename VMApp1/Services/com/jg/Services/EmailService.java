package com.jg.Services;
Package com.jg.Service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {	

	public void sendEmail(String toemail, String subject, String textmessage){
		final String username = "oj.noreply@gmail.com";
		final String password = "journal2journal";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("oj.noreply@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toemail));
			message.setSubject(subject);
			message.setText(textmessage);
			
			Transport.send(message);

			System.out.println("Sent Email to "+toemail);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}