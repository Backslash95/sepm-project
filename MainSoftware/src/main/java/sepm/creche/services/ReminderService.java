package sepm.creche.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sepm.creche.mail.MailService;
import sepm.creche.models.Reminder;
import sepm.creche.repositories.ReminderRepository;

@Component
@Scope("session")
public class ReminderService
{
	@Autowired
	private ReminderRepository reminderRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;

	@SuppressWarnings("deprecation")
	public Collection<Reminder> todaysReminders()
	{
		Date today = new Date();
		Date tomorrow = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);

		tomorrow.setHours(23);
		tomorrow.setMinutes(59);
		tomorrow.setSeconds(59);

		for (Reminder r : reminderRepository.findAll())
		{
			System.out.println(r.getDate());
			System.out.println(today);
			System.out.println(tomorrow);

		}

		return reminderRepository.findTodaysReminders(today, tomorrow);
	}

	public void saveReminder(Reminder reminder)
	{
		reminderRepository.save(reminder);
	}

	@Scheduled(cron = "0 0 0 * * *") // every hour;
	// Sec,Min,Hour,Day of month, Month,
	// Day
	// of Week
	public void remind()
	{
		for (Reminder r : todaysReminders())
		{

			mailService.sendEmail(userService.loadUser(r.getUserID()).getEmail(), "Erinnerung: " + r.getTopic(),
					r.getReminderMessage(), null);

		}
	}
}
