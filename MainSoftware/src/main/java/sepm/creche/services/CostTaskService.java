package sepm.creche.services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.models.Child;
import sepm.creche.models.Task;
import sepm.creche.models.TaskState;
import sepm.creche.models.User;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.CrecheSettingsRepository;
import sepm.creche.repositories.TaskRepository;
import sepm.creche.repositories.TodaysKidsRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.ui.controllers.CrecheSettingsController;
import sepm.creche.ui.controllers.TodaysKidsController;

@Component
@Scope("singleton")
public class CostTaskService
{

	Task task;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ChildRepository childRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TodaysKidsRepository todaysKidsRepository;

	@Autowired
	CrecheSettingsRepository crecheSettingsRepository;

	CrecheSettingsController crecheSettingsController = new CrecheSettingsController();

	TodaysKidsController todaysKidsController = new TodaysKidsController();

	public CostTaskService()
	{

	}

	@Scheduled(cron = "0 0 18 1 * ?") // last day of month on 18:00;
	// Sec,Min,Hour,Day of month, Month, Day of Week
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void createCostTask()
	{

		todaysKidsController.setTodaysKidsRepository(todaysKidsRepository);
		crecheSettingsController.setCrecheSettingsRepository(crecheSettingsRepository);
		todaysKidsController.setCrecheSettingsController(crecheSettingsController);
		todaysKidsController.setChildRepository(childRepository);

		String costOfLunch;
		int amountOfLunch = 0;

		Calendar cal = Calendar.getInstance();
		@SuppressWarnings("deprecation")
		int month = cal.getTime().getMonth() - 1;
		todaysKidsController.setMonth(month);

		Collection<Child> allChilds = new HashSet<Child>();
		allChilds.addAll(childRepository.findAll());

		// current date +7
		cal.add(Calendar.DATE, 7);

		for (Child c : allChilds)
		{

			Set<Task> userTasks = new HashSet<Task>();

			task = new Task();

			task.setAmountOfWorkers(c.getMyParents().size());

			task.setAssignedUsers(c.getMyParents());

			amountOfLunch = todaysKidsController.getAmountOfLunch(c.getChildID());

			costOfLunch = todaysKidsController.getCostOfLunchMonth(c.getChildID());
			task.setName("Mittagessen-Abrechnung");
			task.setDescription("Monatliche Abrechnung der Mittagessen. Ihr Kind " + c.getName() + " war "
					+ amountOfLunch + " mal essen. Dies entspricht Kosten von: " + costOfLunch);

			Date deadline = cal.getTime();
			task.setDeadline(deadline);

			task.setTaskState(TaskState.ASSIGNED);
			if (amountOfLunch >= 1)
			{

				for (User u : c.getMyParents())
				{
					userTasks = u.getMyTasks();
					userTasks.add(task);

					userRepository.findFirstByUsername(u.getUsername()).setMyTasks(userTasks);
				}
				taskRepository.save(task);

			}
		}

	}
}
