package sepm.creche.executors;

import sepm.creche.mail.MailService;
import sepm.creche.models.Task;
import sepm.creche.models.User;

public class NewTaskMailNotifierThread implements Runnable
{

	private MailService mailService;
	private Task task;

	public NewTaskMailNotifierThread(MailService mailService, Task task)
	{
		this.mailService = mailService;
		this.task = task;
	}

	@Override
	public void run()
	{
		for (User u : task.getAssignedUsers())
		{
			if (!u.getSendEmails())
				continue;

			mailService.sendEmail(u.getEmail(), "Eine Aufgabe wurde Ihnen zugeteilt!",
					"Folgende Aufgabe wurde Ihnen zugeteilt:" + task.getName() + "\n" + task.getDescription(), null);

		}
	}

}
