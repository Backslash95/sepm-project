package sepm.creche.executors;

import sepm.creche.mail.MailService;
import sepm.creche.models.User;

public class NewUserMailNotifierThread implements Runnable {

	private MailService mailService;
	private User user;

	public NewUserMailNotifierThread(MailService mailService, User user) {
		this.mailService = mailService;
		this.user = user;
	}

	@Override
	public void run() {

		mailService.sendRegistrationEmail(user);

	}

}
