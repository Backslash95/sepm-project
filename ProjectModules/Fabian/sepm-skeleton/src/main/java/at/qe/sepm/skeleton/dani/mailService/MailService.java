package at.qe.sepm.skeleton.dani.mailService;

import java.io.File;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Contains a method to send a email, with or without an attachment.
 * 
 * @author Daniel
 *
 */

@Component
@Scope("singleton")
public class MailService
{

	@Autowired
	private JavaMailSenderImpl mailSender;

	// general mail function
	public void sendEmail(String emailTo, String subject, String messageText, File file) throws MessagingException
	{
		System.out.println("Sending Mail ...");

		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(msg, true);
		try
		{
			message.setTo(emailTo);
			message.setSubject(subject);
			message.setSentDate(new Date());
			message.setText(messageText);
			if (file.exists())
			{
				message.addAttachment(file.getName(), file);
			}
			mailSender.send(msg);
		} catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// reminder mail for assigned tasks
	public void sendEmailTask(String emailTo, File file) throws MessagingException
	{
		System.out.println("Sending Mail ...");

		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(msg, true);
		try
		{
			message.setTo(emailTo);
			message.setSubject("Kinderkrippe Wunderwuzis - Zugewiesene Aufgaben");
			message.setSentDate(new Date());
			message.setText("Im Anhang befindet sich ein PDF mit Ihren zugewiesenen Aufgaben.");
			if (file.exists())
			{
				message.addAttachment(file.getName(), file);
			}
			mailSender.send(msg);
		} catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// confirmation mail for acquaintances
	public void sendEmailAcquaintancesConfirmed(String emailTo) throws MessagingException
	{
		System.out.println("Sending Mail ...");

		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(msg, true);
		try
		{
			message.setTo(emailTo);
			message.setSubject("Kinderkrippe Wunderwuzis - Bezugsperon bestätigt");
			message.setSentDate(new Date());
			message.setText(
					"Die von Ihnen neu angegebene Bezugsperon wurde soeben bestätigt und ist nun im Stammblatt Ihres Kindes eingetragen.");

			mailSender.send(msg);
		} catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
