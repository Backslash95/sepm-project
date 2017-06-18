package at.qe.sepm.skeleton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Simple mail-service to send e-mails.
 *
 * @author Andr� Potocnik <andre.potocnik@student.uibk.ac.at>, Elias Jochum <elias.jochum@student.uibk.ac.at>
 */
@Component
@Scope("application")
public class MailService {

	@Autowired
	private JavaMailSender javaMailService;

	/**
	 * Simple method to send an e-mail 
	 * 
	 * @param from sender
	 * @param to recipient
	 * @param subject of mail
	 * @param text message
	 */
	public void sendMail(String from, String to, String subject, String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(text);

		try {
			this.javaMailService.send(msg);
		} catch (MailException e) {
			System.out.println(e.getMessage());
		}
	}

}
