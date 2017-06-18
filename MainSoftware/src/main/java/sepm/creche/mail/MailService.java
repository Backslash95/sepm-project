package sepm.creche.mail;

import java.io.File;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.User;

/**
 * Contains a method to send a email, with or without an attachment.
 * 
 * @author Daniel
 *
 */

@Component
@Scope("session")
public class MailService
{

	@Autowired
	private AuditLog auditLog;

	@Autowired
	private JavaMailSenderImpl mailSender;

	// general mail function
	public void sendEmail(String emailTo, String subject, String messageText, File file)
	{
		System.out.println(new Date() + "[Mail-Module] Sending Mails!");

		try
		{
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(msg, true);
			message.setTo(emailTo);
			message.setSubject(subject);
			message.setSentDate(new Date());
			message.setText(messageText);
			if (file != null)
			{
				if (file.exists())
				{
					message.addAttachment(file.getName(), file);
				}
			}
			mailSender.send(msg);
		} catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(new Date() + "[Mail-Module] Mail Sent!");
	}

	// reminder mail for assigned tasks
	public void sendEmailTask(String emailTo, File file)
	{
		System.out.println(new Date() + "[Mail-Module] Sending Mails!");

		try
		{

			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(msg, true);
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
		System.out.println(new Date() + "[Mail-Module] Mail Sent!");
	}

	// confirmation mail for acquaintances
	public void sendEmailAcquaintancesConfirmed(String emailTo)
	{
		System.out.println(new Date() + "[Mail-Module] Sending Mails!");

		try
		{
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(msg, true);
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
		System.out.println(new Date() + "[Mail-Module] Mail Sent!");
	}

	// sends email with account information
	public void sendRegistrationEmail(User user)
	{
		System.out.println(new Date() + "[Mail-Module] Sending Mails!");

		try
		{
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(msg, true);
			message.setTo(user.getEmail());
			message.setSubject("Kinderkrippe Wunderwuzis - Registrierungsinformationen");
			message.setSentDate(new Date());
			message.setText(
					"Sehr geehrte/r Herr/Frau " + user.getLastName() + ",\n\n" + "vielen Dank für Ihre Registrierung.\n"
							+ "Der Login befindet sich unter http://localhost:8080/secured/restricted/ParentData/editParent.xhtml\nIhre Anmeldedaten lauten wie folgt:\n\n"
							+ "Username: " + user.getUsername() + "\n" + "Passwort: " + user.getPassword()
							+ "\n Bitte ändern Sie sofort ihr Passwort.\n\n" + "Vielen Dank,\n" + "Ihre Wunderwuzis.");

			mailSender.send(message.getMimeMessage());
		} catch (MessagingException e)
		{
			e.printStackTrace();
		}
		auditLog.log(user, "Registration email sent to " + user.getEmail());
		System.out.println(new Date() + "[Mail-Module] Mail Sent!");
	}

}
