package com.jg.Services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
			//message.setText(textmessage);
			
			Multipart mp = new MimeMultipart("alternative");
			
			/*// Attach Plain Text
			MimeBodyPart plain = new MimeBodyPart();
			plain.setText(plainText);
			mp.addBodyPart(plain);*/

			/*
			 * Any attached images for the HTML portion of the email need to be encapsulated with
			 * the HTML portion within a 'related' MimeMultipart. Hence we create one of these and
			 * set it as a bodypart for the overall message.
			 */
			MimeMultipart htmlmp = new MimeMultipart("related");
			MimeBodyPart htmlbp = new MimeBodyPart();
			htmlbp.setContent(htmlmp);
			mp.addBodyPart(htmlbp);

			// Attach HTML Text
			MimeBodyPart html = new MimeBodyPart();
			html.setContent(textmessage, "text/html");
			htmlmp.addBodyPart(html);
			
			/*
			// Attach template images (EmailImage is a simple class that holds image data)
			for (EmailImage ei : template.getImages()) {
			    MimeBodyPart img = new MimeBodyPart();
			    img.setContentID(ei.getFilename());
			    img.setFileName(ei.getFilename());
			    ByteArrayDataSource bads = new ByteArrayDataSource(ei.getImageData(), ei.getMimeType());
			    img.setDataHandler(new DataHandler(bads));
			    htmlmp.addBodyPart(img);
			}
			*/	
			
			message.setContent(mp);
			
			Transport.send(message);

			System.out.println("Sent Email to "+toemail);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}